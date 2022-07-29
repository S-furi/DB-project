package db_project.view;

import db_project.db.ConnectionProvider;

public class ConnectionController {
<<<<<<< HEAD
    private static final String username = "root";
    private static final String password = "Spongebobluca01!";
    private static final String dbName = "users";
=======
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "users";
>>>>>>> 2a2de119376de7b282f23250f9db42b0be8d484c

  public ConnectionController() {}

  public static ConnectionProvider getConnectionProvider() {
    return new ConnectionProvider(username, password, dbName);
  }
}
