package db_project.db.queryUtils;

import java.util.List;
import java.util.Optional;

import javafx.util.Pair;

public class QueryResult {
    private Optional<List<List<Pair<String, Object>>>> result;

    public QueryResult() {
        this.result = Optional.empty();
    }

    public void buildResult(final List<List<Pair<String, Object>>> res) {
        this.result = Optional.of(res);
    }

    public Optional<List<List<Pair<String, Object>>>> getResult() {
        return result;
    }
}
