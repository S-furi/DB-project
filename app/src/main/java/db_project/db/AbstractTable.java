package db_project.db;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import db_project.db.queryUtils.ArrayQueryParser;
import db_project.db.queryUtils.QueryParser;
import db_project.db.queryUtils.QueryResult;

public abstract class AbstractTable<T, K> implements Table<T, K> {
  private String tableName;
  private final QueryParser parser;
  private String primaryKeyName;
  private boolean isSetUpDone;
  private List<String> tableColumns;

  public AbstractTable(final String tableName, final Connection connection) {
    this.tableName = tableName;
    this.parser = new ArrayQueryParser(connection);
    this.isSetUpDone = false;
  }

  public void setPrimaryKey(final String primaryKey) {
    this.primaryKeyName = primaryKey;
    this.isSetUpDone = true;
  }

  public void setTableColumns(List<String> tableColumns) {
    this.tableColumns = tableColumns;
  }

  @Override
  public String getTableName() {
    return this.tableName;
  }

  @Override
  public Optional<T> findByPrimaryKey(final K primaryKey) {
    if (!this.isSetUpDone) {
      throw new IllegalStateException("Table's primaryKey is not set!");
    }
    final String query =
        "SELECT * FROM " + this.tableName + " WHERE " + this.primaryKeyName + " = ?";
    final Object[] params = {primaryKey};
    this.parser.computeSqlQuery(query, params);
    return this.getPrettyResultFromQueryResult(this.parser.getQueryResult()).stream().findAny();
  }

  @Override
  public List<T> findAll() {
    if (!this.isSetUpDone) {
      throw new IllegalStateException("Table's primaryKey is not set!");
    }
    final String query = "SELECT * FROM " + this.tableName;
    this.parser.computeSqlQuery(query, null);
    return this.getPrettyResultFromQueryResult(this.parser.getQueryResult());
  }

  @Override
  public boolean save(final T value) {
    if (!this.isSetUpDone) {
      throw new IllegalStateException("Table's primaryKey is not set!");
    }
    final StringBuilder query = new StringBuilder();
    query.append("INSERT INTO " + this.tableName + " (" + this.primaryKeyName + ",");
    this.tableColumns.forEach(t -> query.append(t + ","));
    query.deleteCharAt(query.length() - 1);
    query.append(") VALUES (?,");
    this.tableColumns.forEach(t -> query.append("?,"));
    query.deleteCharAt(query.length() - 1);
    query.append(")");

    final Object[] params = this.getSaveQueryParameters(value);
    try {
      return this.parser.computeSqlQuery(query.toString(), params);
    } catch (IllegalStateException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Return all the parameters for Save query (all the info about Parameter);
   *
   * @param value the value where to retreive the parameters.
   * @return the list of paramentes.
   */
  protected abstract Object[] getSaveQueryParameters(T value);

  @Override
  public boolean update(final T updatedValue) {
    final StringBuilder query = new StringBuilder();
    query.append("UPDATE " + this.tableName + " SET ");
    this.tableColumns.forEach(t -> query.append(t + " = ?,"));
    query.deleteCharAt(query.length() - 1);
    query.append(" WHERE " + this.primaryKeyName + " = ?");

    final Object[] params = this.getUpdateQueryParameters(updatedValue);
    try {
      return this.parser.computeSqlQuery(query.toString(), params);
    } catch (IllegalStateException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   * Return all the parameters for Update query (all info about the parameter except the primary
   * key);
   *
   * @param updatedValue
   * @return
   */
  protected abstract Object[] getUpdateQueryParameters(final T updatedValue);

  @Override
  public boolean delete(final K primaryKey) {
    if (!this.isSetUpDone) {
      throw new IllegalStateException("Table's primaryKey is not set!");
    }
    final String query = "DELETE FROM " + this.tableName + " WHERE " + this.primaryKeyName + " = ?";
    final Object[] params = {primaryKey};
    try {
      return this.parser.computeSqlQuery(query, params);
    } catch (IllegalStateException e) {
        throw new IllegalStateException(e);
    } 
  }

  /**
   * Determine how to interpret and read the result given from a query;
   *
   * @param result the result of the computed query
   * @return a list of human readeable results.
   */
  protected abstract List<T> getPrettyResultFromQueryResult(final QueryResult result);
}
