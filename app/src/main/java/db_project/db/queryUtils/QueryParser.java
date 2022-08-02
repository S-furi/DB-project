package db_project.db.queryUtils;

public interface QueryParser {
  /**
   * Computing the given SQL query. In case of non-static-final variables
   * (prepared statements) type the query with those values with a '?',
   * and place them in the right order in an array of Objects as a second
   * parameter.
   * 
   * @param query the SQL query to compute
   * @param params the non-static-final parameters of the query
   * @return true if the query succeded, false otherwise
   */
  public boolean computeSqlQuery(final String query, final Object[] params);

  /**
   * If the computeSqlQuery() was called, this methods returns
   * an object of type {@link db_project.db.queryUtils.QueryResult}
   * modeling the actual result given from the query.
   * 
   * @return a {@link db_project.db.queryUtils.QueryResult} modeling query's result.
   */
  public QueryResult getQueryResult();

  /**
   * 
   * @return the given query; null otherwise...
   */
  public String getQuery();

  /**
   * Discard saved query.
   */
  public void resetQuery();
}
