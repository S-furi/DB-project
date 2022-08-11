package db_project.db;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

import db_project.db.queryUtils.ArrayQueryParser;
import db_project.db.queryUtils.QueryParser;
import db_project.db.queryUtils.QueryResult;

public abstract class AbstractCompositeKeyTable<T, K> implements CompositeKeyTable<T, K> {
  private final String tableName;
  protected final QueryParser parser;
  private List<String> keyNames;
  private boolean isSetUpDone;
  private List<String> tableColumns;
  protected boolean created;

  public AbstractCompositeKeyTable(final String tableName, final Connection connection) {
    this.tableName = tableName;
    this.parser = new ArrayQueryParser(connection);
    this.isSetUpDone = false;
    this.created = false;
  }

  public void setPrimaryKey(final List<String> keyNames) {
    this.keyNames = List.copyOf(keyNames);
    this.isSetUpDone = true;
  }

  public void setTableColumns(final List<String> tableColumns) {
    this.tableColumns = List.copyOf(tableColumns);
  }

  @Override
  public String getTableName() {
    return this.tableName;
  }

  @Override
  public Optional<T> findByPrimaryKey(final List<K> primaryKeys) {
    if (!this.isSetUpDone) {
      throw new IllegalStateException("Table's primaryKey is not set!");
    }
    final StringBuilder query = new StringBuilder();
    query.append("SELECT * FROM " + this.tableName + " WHERE ");
    final var firstKey = this.keyNames.get(0);
    query.append(firstKey + " = ?");
    this.keyNames.stream()
        .filter(t -> !t.equals(firstKey))
        .forEach(keyName -> query.append(" AND " + keyName + " = ?"));

    final Object params[] = primaryKeys.toArray();
    this.parser.computeSqlQuery(query.toString(), params);
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
    query.append("INSERT INTO " + this.tableName + " (");
    this.keyNames.forEach(keyName -> query.append(keyName + ","));
    this.tableColumns.forEach(colName -> query.append(colName + ","));
    query.deleteCharAt(query.length() - 1);
    query.append(") VALUES (");
    final int totalNumOfCols = this.keyNames.size() + this.tableColumns.size();
    for (var i = 0; i < totalNumOfCols - 1; i++) {
      query.append("?,");
    }
    query.append("?)");

    final Object[] params = this.getSaveQueryParameters(value);

    return this.parser.computeSqlQuery(query.toString(), params);
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
    if (!this.isSetUpDone) {
      throw new IllegalStateException("Table's primaryKey is not set!");
    }
    final StringBuilder query = new StringBuilder();
    query.append("UPDATE " + this.tableName + " SET ");
    this.keyNames.forEach(t -> query.append(t + " = ?,"));
    this.tableColumns.forEach(t -> query.append(t + " = ?,"));
    query.deleteCharAt(query.length() - 1);
    final var firstKey = this.keyNames.get(0);
    query.append(" WHERE " + firstKey + " = ? ");
    this.keyNames.stream()
        .filter(t -> !t.equals(firstKey))
        .forEach(t -> query.append("AND " + t + " = ? "));
    final Object[] params = this.getUpdateQueryParameters(updatedValue);

    return this.parser.computeSqlQuery(query.toString(), params);
  }

  /**
   * Return all the parameters for Update query (all the info about Parameter); In this specific
   * case, the structure of parameters is the following (assuming to have 2 keys and 2 columns):
   * final Object[] params = { key1, key2, col1, col2, key1, key2 };
   *
   * <p>The syntax is weird, but the query can explain why: UPDATE TABLE_NAME SET key1=?, key2=?,
   * col1=?, col2=? WHERE key1 = ? AND key2 = ?
   *
   * <p>The set on the keys is necessary because the are instances in out DB where a table is formed
   * only by a 3 primary keys, and if you want to change a parameter (e.g. the date forms the PK)
   * it's necessary to specify all the keys (apparently); No better ways was found :) .
   *
   * @param value the value where to retreive the parameters.
   * @return the list of paramentes.
   */
  protected abstract Object[] getUpdateQueryParameters(final T value);

  @Override
  public boolean delete(final List<K> primaryKey) {
    if (!this.isSetUpDone) {
      throw new IllegalStateException("Table's primaryKey is not set!");
    }
    final StringBuilder query = new StringBuilder();
    final var firstKey = this.keyNames.get(0);
    query.append("DELETE FROM " + this.tableName + " WHERE " + firstKey + " = ? ");
    this.keyNames.stream()
        .filter(t -> !t.equals(firstKey))
        .forEach(t -> query.append("AND " + t + " = ?"));
    final Object[] params = primaryKey.toArray();

    return this.parser.computeSqlQuery(query.toString(), params);
  }

  @Override
  public boolean dropTable() {
    final String query = "DROP TABLE " + this.tableName;
    return this.parser.computeSqlQuery(query, null);
  }

  public boolean isCreated() {
    return this.created;
  }

  /**
   * Determine how to interpret and read the result given from a query;
   *
   * @param result the result of the computed query
   * @return a list of human readeable results.
   */
  protected abstract List<T> getPrettyResultFromQueryResult(final QueryResult result);
}
