package db_project.db.queryUtils;

import db_project.db.ConnectionProvider;
import junit.framework.Assert;

public class TestParser {
    private final static String TABLE_NAME = "users";

    public static void main(String[] args) {
        final var parser = new ArrayQueryParser(getConnection().getMySQLConnection());
        System.out.println("****(1)****");
        Assert.assertTrue("Simple Select Failed", testSelect(parser));
        System.out.println("****(2)****");
        Assert.assertTrue("InsertFailed", testInsert(parser));
    }

    private static boolean testSelect(final QueryParser parser) {
        parser.resetQuery();
        String query = String.format(
                "SELECT * FROM %s WHERE id = ?",
                TABLE_NAME);
        Object[] params = { 987426 };

        boolean res = parser.computeSqlQuery(query, params);
        if (!parser.getQueryResult().getResult().isEmpty()) {
            parser.getQueryResult().getResult().get().forEach(t -> {
                t.forEach(System.out::println);
            });
        }
        return res;
    }

    private static boolean testInsert(final QueryParser parser) {
        parser.resetQuery();
        String query = String.format(
                "INSERT INTO %s (id, firstName, lastName, telephone, email) VALUES (?, ?, ?, ?, ?)",
                TABLE_NAME);
        Object[] params = { 1111, "Merda", "Dio", "1209849812", "merda@dio.paradise.org" };

        return parser.computeSqlQuery(query, params) == selectAll(parser) == deleteLastInsertedItem(parser, params[1]);
    }

    private static boolean deleteLastInsertedItem(final QueryParser parser, Object object) {
        parser.resetQuery();
        String query = String.format(
                "DELETE FROM %s WHERE id = ?",
                TABLE_NAME);
        Object[] params = { 1111 };
        return parser.computeSqlQuery(query, params);
    }

    private static boolean selectAll(final QueryParser parser) {
        parser.resetQuery();
        String query = String.format(
                "SELECT * FROM %s",
                TABLE_NAME);
        boolean res = parser.computeSqlQuery(query, null);
        if (!parser.getQueryResult().getResult().isEmpty()) {
            parser.getQueryResult().getResult().get().forEach(t -> t.forEach(System.out::println));
        }
        return res;
    }

    private static ConnectionProvider getConnection() {
        String username = "root";
        String password = "123Test123";
        String dbName = "users";
        return new ConnectionProvider(username, password, dbName);
    }
}
