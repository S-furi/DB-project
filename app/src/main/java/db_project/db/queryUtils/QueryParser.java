package db_project.db.queryUtils;

public interface QueryParser {

    public boolean computeSqlQuery(final String query, final Object[] params);

    public QueryResult getQueryResult();

    public String getQuery();

    public void resetQuery();
}
