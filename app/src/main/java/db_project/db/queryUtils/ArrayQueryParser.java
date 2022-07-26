package db_project.db.queryUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

public class ArrayQueryParser implements QueryParser {
    private final Connection connection;
    private StringBuilder finalQuery;
    private Object[] params;
    private QueryResult result;

    public ArrayQueryParser(final Connection connection) {
        this.result = new QueryResult();
        this.connection = connection;
        this.finalQuery = new StringBuilder();
    }

    @Override
    public boolean computeSqlQuery(final String query, final Object[] params) {
        this.insertQuery(query, params);
        return this.parseAndExecuteQuery();
    }

    private boolean parseAndExecuteQuery() {
        if (this.finalQuery.toString().startsWith("SELECT")) {
            return this.preparedStatementQuery();
        } else if (this.finalQuery.toString().startsWith("DELETE")
                || this.finalQuery.toString().startsWith("UPDATE")
                || this.finalQuery.toString().startsWith("INSERT")) {
            return this.executeUpdate();
        }
        return false;
    }

    private ArrayQueryParser insertQuery(final String query, final Object[] params) {
        this.finalQuery.append(query);
        this.params = params;
        return this;
    }

    private boolean preparedStatementQuery() {
        if (this.params == null) {
            this.result.buildResult(this.basicStatementQuery());
            return true;
        }
        try (final var statement = connection.prepareStatement(this.finalQuery.toString())) {
            System.out.println("----" + this.finalQuery.toString() + "----");
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

    private List<List<Pair<String, Object>>> basicStatementQuery() {
        try (final var statement = connection.createStatement()) {
            System.out.println("----" + this.finalQuery.toString() + "----");
            ResultSet rs = statement.executeQuery(this.finalQuery.toString());
            return generateResultFromResultSet(rs);

        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private boolean executeUpdate() {
        try (final var statement = connection.prepareStatement(this.finalQuery.toString())) {
            System.out.println("----" + this.finalQuery.toString() + "----");
            for (var i = 0; i < this.params.length; i++) {
                this.setTypeStatement(statement, this.params[i], i + 1);
            }
            return statement.executeUpdate() > 0;
        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Very raw and not Object Oriented method for retreiving and using the right
     * method with preparedStatement (it's possibile also to use setObject but
     * mmm... idk)
     * 
     * @param statement the {@link java.sql.PreparedStatement}
     * @param param
     * @param index
     */
    private void setTypeStatement(final PreparedStatement statement, Object param, int index) {
        try {
            if (param.getClass().equals(String.class)) {
                statement.setString(index, param.toString());
            } else if (param.getClass().equals(Integer.class)) {
                statement.setInt(index, (int) param);
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param res the resultSet of a given Query
     * @return a List of the results, mapped as a (key,value) pair
     *         where key is the name of the column, and value is the value of
     *         the i-th row of the result.
     */
    private List<List<Pair<String, Object>>> generateResultFromResultSet(ResultSet res) {
        List<List<Pair<String, Object>>> results = new ArrayList<>();
        try {
            var md = res.getMetaData();
            while (res.next()) {
                final List<Pair<String, Object>> lst = new ArrayList<>();
                for (var i = 1; i <= md.getColumnCount(); i++) {
                    final String colName = md.getColumnName(i);
                    lst.add(new Pair<String, Object>(colName, res.getObject(i)));
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
