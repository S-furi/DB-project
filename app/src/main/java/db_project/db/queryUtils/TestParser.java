package db_project.db.queryUtils;

import db_project.db.ConnectionProvider;
import junit.framework.Assert;

public class TestParser {
  private static final String TABLE_NAME = "users";
  private static final QueryParser parser =
      new ArrayQueryParser(getConnection().getMySQLConnection());

  public static void main(String[] args) {
    System.out.println("****(1)****");
    Assert.assertTrue("Simple Select Failed", testSelect(parser));
    System.out.println("****(2)****");
    Assert.assertTrue("InsertFailed", testInsert(parser));
  }

  public static boolean testSelect(final QueryParser parser) {
    parser.resetQuery();
    String query = String.format("SELECT * FROM %s WHERE id = ?", TABLE_NAME);
    Object[] params = {987426};

    boolean res = parser.computeSqlQuery(query, params);
    if (!parser.getQueryResult().getResult().isEmpty()) {
      parser.getQueryResult().getResult().get().forEach(System.out::println);
    }
    return res;
  }

  public static boolean testInsert(final QueryParser parser) {
    parser.resetQuery();
    String query =
        String.format(
            "INSERT INTO %s (id, firstName, lastName, telephone, email) VALUES (?, ?, ?, ?, ?)",
            TABLE_NAME);
    Object[] params = {1111, "Luca", "Rapolla", "1209849812", "luca.rapolla@studio.unibo.it"};

    return parser.computeSqlQuery(query, params)
        == selectAll(parser)
        == deleteLastInsertedItem(parser, params[1]);
  }

  public static boolean deleteLastInsertedItem(final QueryParser parser, Object object) {
    parser.resetQuery();
    String query = String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME);
    Object[] params = {1111};
    return parser.computeSqlQuery(query, params);
  }

  public static boolean selectAll(final QueryParser parser) {
    parser.resetQuery();
    String query = String.format("SELECT * FROM %s", TABLE_NAME);
    boolean res = parser.computeSqlQuery(query, null);
    if (!parser.getQueryResult().getResult().isEmpty()) {
      parser.getQueryResult().getResult().get().forEach(System.out::println);
    }
    return res;
  }

  public static ConnectionProvider getConnection() {
    String username = "root";
    String password = "123Test123";
    String dbName = "users";
    return new ConnectionProvider(username, password, dbName);
  }

  public static QueryParser getParser() {
    return parser;
  }
}
