package db_project.view;

import db_project.db.ConnectionProvider;

public class ConnectionController {
    private static final String username = "root";
    private static final String password = "Spongebobluca01!";
    private static final String dbName = "users";

    public ConnectionController() {
    }

    public static ConnectionProvider getConnectionProvider() {
        return new ConnectionProvider(username, password, dbName);
    }
}
