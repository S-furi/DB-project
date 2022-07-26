package db_project.db.queryUtils;

import java.util.List;

import javafx.util.Pair;

public interface QueryParser {
    /**
     * Execute the created query
     * 
     * @return a List of the results, mapped as a (key,value) pair
     * where key is the name of the column, and value is the value of 
     * the i-th row of the result.
     */
    public List<List<Pair<String, Object>>> getQueryResult();

    /**
     * 
     * @param query
     * @param params
     * @return
     */
    public ArrayQueryParser insertQuery(final String query, final Object[] params);

    public String getQuery();

    public void resetQuery();
}
