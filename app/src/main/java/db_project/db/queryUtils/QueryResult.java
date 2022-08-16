package db_project.db.queryUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QueryResult {
  private Optional<List<Map<String, Object>>> result;

  public QueryResult() {
    this.result = Optional.empty();
  }

  /**
   * Kind of a setter
   *
   * @param res
   */
  public void buildResult(final List<Map<String, Object>> res) {
    this.result = Optional.of(res);
  }

  /**
   * Get the query result stored by this object. Speaking about the type: It's a list that contains
   * a list of pairs. The first list models the results (rows) the query yielded. In case a find by
   * primary key is called, the outer list only contains one element. The second list models the
   * columns of the current row. Every element of this second list is a pair: the key of it is the
   * name of the current column, and the value is the value (hehe) of the row-column. No better
   * ideas came to my mind when I was thinking about this...
   *
   * @return The result stored by this object (a portion of a table). {@link
   *     java.util.Optional#empty()} if there isn't a result.
   */
  public Optional<List<Map<String, Object>>> getResult() {
    return result;
  }

  @Override
  public String toString() {
    final StringBuilder res = new StringBuilder();
    result.get().forEach(t -> {
      t.forEach((k, v) -> {
        res.append("(+"+ k + v +")");
      });
      res.append("\n");
    });
    return res.toString();
  }  
}
