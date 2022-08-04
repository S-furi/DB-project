package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Driver;
import db_project.utils.Utils;

public class DriverTableTest {
    private static final String username = "root";
    private static final String password = "123Test123";
    private static final String dbName = "Ferrovia";
  
    private final ConnectionProvider connectionProvider =
        new ConnectionProvider(username, password, dbName);

    private final DriverTable driverTable = 
        new DriverTable(this.connectionProvider.getMySQLConnection());

    private final Driver driver = new Driver(
        "3", 
        Utils.buildDate(31, 12, 1992).get(), 
        "stefano", 
        "furi", 
        123,
        "stefano.furi7@gmail.com", 
        "C");

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
    public void testSaveAndDelete() {
        assertTrue(this.driverTable.save(this.driver));
        assertThrows(IllegalStateException.class, () -> {
            this.driverTable.save(this.driver);
        });
        //deleting
        assertTrue(this.driverTable.delete(this.driver.getLicenceNumber()));
        assertFalse(this.driverTable.delete(this.driver.getLicenceNumber()));
    }

    @Test
    public void testUpdate() {
        final var currDriver = this.driverTable.findByPrimaryKey("2");
        if (currDriver.isEmpty()) {
            fail("Select Failed!");
        }
        boolean result = this.driverTable.update(
            new Driver(
                currDriver.get().getLicenceNumber(), 
                this.driver.getContractYear(), 
                this.driver.getFirstName(), 
                this.driver.getLastName(), 
                this.driver.getTelephone(), 
                this.driver.getEmail(), 
                this.driver.getResidence()));
        assertTrue(result);
        assertTrue(this.driverTable.update(currDriver.get()));
    }
}
