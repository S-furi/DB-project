package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Driver;
import db_project.utils.Utils;

public class DriverTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);

  private static final DriverTable driverTable =
      new DriverTable(connectionProvider.getMySQLConnection());

  private final Driver driver =
      new Driver(
          "3",
          Utils.buildDate(31, 12, 1992).get(),
          "stefano",
          "furi",
          123,
          "stefano.furi7@gmail.com",
          "C");

  @BeforeAll
  static void setUp() {
    //city dependency
    CityTableTest.setUp();
    final var driver1 =
        new Driver(
            "1",
            Utils.buildDate(21, 11, 1990).get(),
            "mizzico",
            "mizzichi",
            12,
            "nonhovoglia@gmail.com",
            "C");
    final var driver2 =
        new Driver(
            "2",
            Utils.buildDate(22, 11, 1991).get(),
            "ariostrio",
            "iostrio",
            11,
            "nonohovogliaa@gmail.com",
            "C");

    assertTrue(driverTable.save(driver1));
    assertTrue(driverTable.save(driver2));
  }

  @AfterAll
  static void tearDown() {
    driverTable.findAll().forEach(t -> driverTable.delete(t.getLicenceNumber()));
    //city dependency
    CityTableTest.tearDown();
  }

  @Test
  public void testFindAll() {
    assertFalse(driverTable.findAll().isEmpty());
    assertTrue(driverTable.findAll().size() == 2);
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(driverTable.findByPrimaryKey("1").isPresent());
  }

  @Test
  public void testSaveAndDelete() {
    assertTrue(driverTable.save(this.driver));
    assertThrows(
        IllegalStateException.class,
        () -> {
          driverTable.save(this.driver);
        });
    // deleting
    assertTrue(driverTable.delete(this.driver.getLicenceNumber()));
    assertFalse(driverTable.delete(this.driver.getLicenceNumber()));
  }

  @Test
  public void testUpdate() {
    final var currDriver = driverTable.findByPrimaryKey("2");
    if (currDriver.isEmpty()) {
      fail("Select Failed!");
    }
    boolean result =
        driverTable.update(
            new Driver(
                currDriver.get().getLicenceNumber(),
                this.driver.getContractYear(),
                this.driver.getFirstName(),
                this.driver.getLastName(),
                this.driver.getTelephone(),
                this.driver.getEmail(),
                this.driver.getResidence()));
    assertTrue(result);
    assertTrue(driverTable.update(currDriver.get()));
  }
}
