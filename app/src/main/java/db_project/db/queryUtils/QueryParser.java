package db_project.db.queryUtils;

import java.util.List;
import java.util.Optional;

import javafx.util.Pair;

public interface QueryParser {

    public boolean computeSqlQuery(final String query, final Object[] params);

    public Optional<List<List<Pair<String, Object>>>> getResult();

    public String getQuery();

    public void resetQuery();
}
