package db_project.db.queryUtils;

import db_project.db.ConnectionProvider;
import junit.framework.Assert;

public class TestParser {
    private final static String TABLE_NAME = "Students";

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
        Object[] params = {987426};

        boolean res = parser.computeSqlQuery(query, params);
        if (!parser.getResult().isEmpty()) {
            parser.getResult().get().forEach(t -> {
                t.forEach(k -> {
                    System.out.println(
                        String.format("%s => %s", k.getKey(), k.getValue())
                    );
                });
            });
        }
        return res;
    }

    private static boolean testInsert(ArrayQueryParser parser) {
        parser.resetQuery();
        String query = String.format(
            "INSERT INTO %s (id, firstName, lastName) VALUES (?, ?, ?)", 
            TABLE_NAME);
        Object[] params = {1111, "Merda", "Dio"};

        return parser.computeSqlQuery(query, params) == deleteLastInsertedItem(parser, params[1]);
    }

    private static boolean deleteLastInsertedItem(ArrayQueryParser parser, Object object) {
        parser.resetQuery();
        String query = String.format(
            "DELETE FROM %s WHERE id = ?", 
            TABLE_NAME);
        Object[] params = {1111};
        return parser.computeSqlQuery(query, params);
    }

    private static ConnectionProvider getConnection() {
        String username = "root";
        String password = "123Test123";
        String dbName = "labjdbc";
        return new ConnectionProvider(username, password, dbName);
    }
}
