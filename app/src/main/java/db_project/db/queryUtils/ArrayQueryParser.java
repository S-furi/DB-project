package db_project.db.queryUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArrayQueryParser implements QueryParser {
  private final Connection connection;
  private StringBuilder finalQuery;
  private Object[] params;
  private QueryResult result;
  private final Logger logger = Logger.getLogger("QueryParser");

  public ArrayQueryParser(final Connection connection) {
    this.result = new QueryResult();
    this.connection = connection;
    this.finalQuery = new StringBuilder();
    logger.setLevel(Level.WARNING);
  }

  @Override
  public boolean computeSqlQuery(final String query, final Object[] params) {
    this.insertQuery(query, params);
    return this.parseAndExecuteQuery();
  }

  /**
   * Super bad method, but it works... Check with wich keyword the given query begins with, an
   * according to that, an update is executed (in case of DELETE, UPDATE, INSERT operations) or a
   * result is built
   *
   * @return false if the query starts with an unknown keyword
   */
  private boolean parseAndExecuteQuery() {
    if (this.finalQuery.toString().startsWith("SELECT")) {
      return this.variableStatementQuery();
    } else if (this.finalQuery.toString().startsWith("DELETE")
        || this.finalQuery.toString().startsWith("UPDATE")
        || this.finalQuery.toString().startsWith("INSERT")
        || this.finalQuery.toString().startsWith("create")
        || this.finalQuery.toString().startsWith("DROP")) {
      return this.executeUpdate();
    }
    return false;
  }

  private ArrayQueryParser insertQuery(final String query, final Object[] params) {
    this.finalQuery.append(query);
    this.params = params;
    return this;
  }

  /**
   * It's called variabile because it's been called if the query is Basic (it hasn't got
   * non-static-final fields inside query's body) or it needs to be a PreparedStatement, in order to
   * insert all the parameters.
   *
   * @return true if the statement is created succefully
   * @throws IllegalStateException if a SQLException is raised.
   */
  private boolean variableStatementQuery() {
    if (this.params == null) {
      this.result.buildResult(this.basicStatementQuery());
      return true;
    }
    try (final var statement = connection.prepareStatement(this.finalQuery.toString())) {
      this.logger.info("----" + this.finalQuery.toString() + "----");
      for (var i = 0; i < this.params.length; i++) {
        this.setTypeStatement(statement, this.params[i], i + 1);
      }
      ResultSet rs = statement.executeQuery();
      this.result.buildResult(generateResultFromResultSet(rs));
      return true;
    } catch (final SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Generate a result from a Basic Statement query (means that the query hasn't got
   * non-static-final parameters and so it's safe (no sql injection) to assign them to the string).
   *
   * <p>For a better understanding about the result go to {@link
   * db_project.db.queryUtils.QueryResult} and read why and what it yields.
   *
   * @return the result given from the query
   */
  private List<Map<String, Object>> basicStatementQuery() {
    try (final var statement = connection.createStatement()) {
      this.logger.info("----" + this.finalQuery.toString() + "----");
      ResultSet rs = statement.executeQuery(this.finalQuery.toString());
      return generateResultFromResultSet(rs);

    } catch (final SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Method called only if the query is an update-query (INSERT, DELETE, UPDATE).
   *
   * @return true if everything goes ok
   * @throws IllegalStateException if a SQLException is thown
   */
  private boolean executeUpdate() {
    if (params == null) {
      return this.executeBasicUpdate();
    }
    try (final var statement = connection.prepareStatement(this.finalQuery.toString())) {
      this.logger.info("----" + this.finalQuery.toString() + "----");
      for (var i = 0; i < this.params.length; i++) {
        this.setTypeStatement(statement, this.params[i], i + 1);
      }
      this.resetQuery();
      return statement.executeUpdate() > 0;
    } catch (final SQLException e) {
      // Error code 1062 is the Duplicate primary key insertion.
      // Error code 1451 is the Constraint Violation when deleting.
      if (!(e.getErrorCode() == 1451) && !(e.getErrorCode() == 1062)) {
        throw new IllegalStateException(e);
      }
      if (e.getErrorCode() == 1062) {
        this.logger.info(e.getMessage());
      } else {
        this.logger.info(e.getMessage());
      }
      return false;
    }
  }

  private boolean executeBasicUpdate() {
    try (final var statement = connection.createStatement()) {
      this.logger.info("----" + this.finalQuery.toString() + "----");
      statement.executeUpdate(this.finalQuery.toString());
      this.resetQuery();
      return true;
    } catch (final SQLException e) {
      if (e.getErrorCode() == 1050) {
        this.logger.info("The table already exists!");
        return true;
      }
      throw new IllegalStateException(e);
    }
  }

  /**
   * Very raw and not Object Oriented method for retreiving and using the right method with
   * preparedStatement (it's possibile also to use setObject but mmm... idk)
   *
   * @param statement the {@link java.sql.PreparedStatement}
   * @param param
   * @param index
   */
  @SuppressWarnings("unchecked")
  private void setTypeStatement(
      final PreparedStatement statement, final Object param, final int index) {
    try {
      if (param == null) {
        statement.setNull(index, java.sql.Types.VARCHAR);
      } else if (param.getClass().equals(String.class)) {
        statement.setString(index, param.toString());
      } else if (param.getClass().equals(Integer.class)) {
        statement.setInt(index, (int) param);
      } else if (param.getClass().equals(java.sql.Date.class)) {
        statement.setDate(index, (java.sql.Date) param);
      } else if (param.getClass().equals(Float.class)) {
        statement.setDouble(index, (Float) param);
      } else if (param.getClass().equals(java.util.Optional.class)) {
        final Optional<String> optParam = (Optional<String>) param;
        if (optParam.isPresent()) {
          statement.setString(index, optParam.get());
        } else {
          statement.setNull(index, java.sql.Types.VARCHAR);
        }
      } else {
        throw new SQLException();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * @param res the resultSet of a given Query
   * @return a List of the results, mapped as a (key,value) pair where key is the name of the
   *     column, and value is the value of the i-th row of the result.
   */
  private List<Map<String, Object>> generateResultFromResultSet(ResultSet res) {
    List<Map<String, Object>> results = new ArrayList<>();
    try {
      var md = res.getMetaData();
      while (res.next()) {
        final Map<String, Object> lst = new HashMap<>();
        for (var i = 1; i <= md.getColumnCount(); i++) {
          final String colName = md.getColumnName(i);
          lst.put(colName, res.getObject(i));
        }
        results.add(lst);
      }
      return results;
    } catch (SQLException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public QueryResult getQueryResult() {
    this.resetQuery();
    this.logger.info(this.result.toString());
    return this.result;
  }

  @Override
  public String getQuery() {
    return this.finalQuery.toString();
  }

  @Override
  public void resetQuery() {
    this.finalQuery = new StringBuilder();
    this.params = null;
  }
}
