package db_project.db.queryUtils;

import java.util.Objects;
import db_project.db.ConnectionProvider;

public class TestParser {
    public static void main(String[] args) {
        final String tableName = "Shippers";
        String query = String.format("SELECT * FROM %s WHERE ShipperID=?", tableName);
        final QueryParser parser = new ArrayQueryParser(getConnection().getMySQLConnection());
        Object[] parms = { 1 };

        var res = Objects.requireNonNull(
            parser.insertQuery(query, parms).getQueryResult()
        );
        res.forEach((t -> {
            System.out.println("----------------");
            t.forEach(v -> System.out.println(String.format("\t(%s, %s)", v.getKey(), v.getValue())));
        } ));
    }

    private static ConnectionProvider getConnection() {
        String username = "root";
        String password = "123Test123";
        String dbName = "northwind";
        return new ConnectionProvider(username, password, dbName);
    }
}
