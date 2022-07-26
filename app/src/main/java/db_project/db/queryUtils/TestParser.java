package db_project.db.queryUtils;

import java.util.Objects;

import db_project.db.ConnectionProvider;
import junit.framework.Assert;

public class TestParser {
    private final static String TABLE_NAME = "Students";

    public static void main(String[] args) {
        final var parser = new ArrayQueryParser(getConnection().getMySQLConnection());
        System.out.println("****(1)****");
        Assert.assertTrue("Simple Select Failed", testSelect(parser)); 
        parser.resetQuery();
        System.out.println("****(2)****");
        Assert.assertTrue("InsertFailed", testInsert(parser));
    }

    private static boolean testInsert(ArrayQueryParser parser) {
        String query = String.format("INSERT INTO %s (id, firstName, lastName) VALUES (?, ?, ?)", TABLE_NAME);
        Object[] params = {11111, "Merda", "Dio"};
        var res = parser.insertQuery(query, params);
        res.executeQuery();
        Assert.assertTrue("selectAll failed", testSelectAll(parser));
        Assert.assertTrue("delete Failed", removeLastAdded(params[0], parser));
        return true;
    }

    private static boolean removeLastAdded(final Object id, final ArrayQueryParser parser) {
        try {
            parser.resetQuery();
            String query = String.format("DELETE FROM %s WHERE id = ?", TABLE_NAME);
            Object[] params = {id};
            parser.insertQuery(query, params);
            parser.executeQuery();
            return true;
        } catch (final Exception e) {
            return false;
        }
    }

    private static boolean testSelectAll(ArrayQueryParser parser) {
        parser.resetQuery();
        final String TABLE_NAME = "Students";
        String query = String.format("SELECT id, firstName, lastName FROM %s", TABLE_NAME);

        try {
            var res = Objects.requireNonNull(
                parser.insertQuery(query, null).getQueryResult()
            );
            res.forEach((t -> {
                System.out.println("----------------");
                t.forEach(v -> System.out.println(String.format("\t(%s, %s)", v.getKey(), v.getValue())));
            } ));
            return true;
        } catch (final Exception e) {
            return false;
        }
    }



    private static boolean testSelect(final ArrayQueryParser parser) {
        final String TABLE_NAME = "Students";
        String query = String.format("SELECT id, firstName, lastName FROM %s WHERE firstName=?", TABLE_NAME);
        Object[] parms = { "Stefano" };

        try {
            var res = Objects.requireNonNull(
                parser.insertQuery(query, parms).getQueryResult()
            );
            res.forEach((t -> {
                System.out.println("----------------");
                t.forEach(v -> System.out.println(String.format("\t(%s, %s)", v.getKey(), v.getValue())));
            } ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static ConnectionProvider getConnection() {
        String username = "root";
        String password = "123Test123";
        String dbName = "labjdbc";
        return new ConnectionProvider(username, password, dbName);
    }
}
