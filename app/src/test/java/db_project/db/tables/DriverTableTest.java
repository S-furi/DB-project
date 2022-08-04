package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

import db_project.db.ConnectionProvider;

public class DriverTableTest {
    private static final String username = "root";
    private static final String password = "123Test123";
    private static final String dbName = "Ferrovia";
  
    private final ConnectionProvider connectionProvider =
        new ConnectionProvider(username, password, dbName);

    private final DriverTable driverTable = 
        new DriverTable(this.connectionProvider.getMySQLConnection());

    @Test
    public void testDelete() {

    }

    @Test
    public void testFindAll() {
        assertFalse(this.driverTable.findAll().isEmpty());
        // in our tests, the DB is filled with 2 elements
        assertTrue(this.driverTable.findAll().size() == 2);
    }

    @Test
    public void testFindByPrimaryKey() {
        assertTrue(this.driverTable.findByPrimaryKey("1").isPresent());
    }

    @Test
    public void testGetTableName() {

    }

    @Test
    public void testSave() {

    }

    @Test
    public void testUpdate() {

    }
}
