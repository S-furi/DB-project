package db_project.db.queryUtils;

import java.util.Objects;
import db_project.db.ConnectionProvider;

public class TestParser {
    public static void main(String[] args) {
        var parser = new QueryParser(getConnection().getMySQLConnection());
        var tableName = "Employees";
        Objects.requireNonNull(parser.select("*", "")
            .from(tableName)
            .where("EmployeeID = ?", 1)
            .getQueryResult()).forEach(t -> {
               System.out.println(String.format("(%s, %s)", t.getKey(), t.getValue()));
            });
    }

    private static ConnectionProvider getConnection() {
        String username = "root";
        String password = "123Test123";
        String dbName = "northwind";
        return new ConnectionProvider(username, password, dbName);
    }
}
