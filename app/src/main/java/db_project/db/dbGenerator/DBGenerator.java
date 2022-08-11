package db_project.db.dbGenerator;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBGenerator {
  private final String USERNAME = "root";
  private final String PASSWORD = "123Test123";
  private final String DBNAME = "Railway";
  private final String URI = "jdbc:mysql://localhost:3306/";
  private final Logger logger = Logger.getLogger("DBGenerator");

  public DBGenerator() {
    logger.setLevel(Level.INFO);
  }

  public boolean createDB() {
    final String query = "CREATE DATABASE " + DBNAME;
    final boolean res = connectAndExecuteUpdate(query);
    logger.info(res ? DBNAME + " Creation Succeed!" : DBNAME + " Creation Failed: DB already exists");
    return res;
  }

  public boolean dropDB() {
    final String query = "DROP DATABASE " + DBNAME;
    final boolean res = connectAndExecuteUpdate(query);
    logger.info(res ? DBNAME + " Drop Succeed!" : DBNAME + " Drop Failed: DB doensn't exist");
    return res;
  }


  private boolean connectAndExecuteUpdate(final String query) {
    try {
        final var connection = DriverManager.getConnection(URI, USERNAME, PASSWORD);
        if (connection == null) {
          throw new IllegalStateException("Connection Failed");
        }
        final var resultSet = connection.getMetaData().getCatalogs();
        while (resultSet.next()) {
          final String catalog = resultSet.getString(1);
          if (catalog.equals(DBNAME)) {
            logger.info("DB " + DBNAME + " cant'b de dropped because it doesn't exists!");
            return false;
          }
        }
  
        final var statement = connection.createStatement();
        statement.executeUpdate(query);
        logger.info("Succefully dropped DB: " + DBNAME);
        return true;
      } catch (final SQLException e) {
        throw new IllegalStateException(e);
      }
  }
}
