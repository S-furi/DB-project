package db_project.db.dbGenerator;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBGenerator {
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123Test123";
    private static final String DBNAME = "Railway";
    private static final String URI = "jdbc:mysql://localhost:3306/";
    private static final Logger logger = Logger.getLogger("DBGenerator");
    

    public static void createDB() {
        logger.setLevel(Level.INFO);
        try {
            final var connection = DriverManager.getConnection(URI, USERNAME, PASSWORD);
            if (connection == null) {
                throw new IllegalStateException("Connection Failed");
            }
            final var resultSet = connection.getMetaData().getCatalogs();
            while (resultSet.next()) {
                final String catalog = resultSet.getString(1);
                if (catalog.equals(DBNAME)) {
                    logger.info("DB " + DBNAME + " exists already!");
                    return;
                }
            }

            final var statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE " + DBNAME);
            logger.info("Succefully Created DB: " + DBNAME);

        } catch (final SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
