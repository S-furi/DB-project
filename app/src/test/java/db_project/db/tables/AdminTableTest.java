package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Admin;
import db_project.utils.Utils;

public class AdminTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);

  private static final AdminTable adminTable =
      new AdminTable(connectionProvider.getMySQLConnection());

  private final Date date = Utils.buildDate(12, 12, 2012).get();
  private final Admin adm =
      new Admin("3", date, "stefano", "furi", 35, "stefano.furi7@gmail.com", "C");

  @BeforeAll
  static void setUp() {
    //city dependency
    CityTableTest.setUp();
    final var adm1 =
        new Admin(
            "1",
            Utils.buildDate(21, 01, 1999).get(),
            "Mario",
            "Rossi",
            65,
            "mario.rossi@gmail.com",
            "C");

    final var adm2 =
        new Admin(
            "2",
            Utils.buildDate(22, 02, 1999).get(),
            "Filippo",
            "Rossi",
            12,
            "filippo.rossi@gmail.com",
            "A");

    assertTrue(adminTable.save(adm1));
    assertTrue(adminTable.save(adm2));
  }

  @AfterAll
  static void tearDown() {
    adminTable.findAll().forEach(t -> adminTable.delete(t.getId()));
    //city dependency
    CityTableTest.tearDown();
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(adminTable.findByPrimaryKey("1").isPresent());
    assertFalse(adminTable.findByPrimaryKey("9").isPresent());
  }

  @Test
  public void testFindAll() {
    assertFalse(adminTable.findAll().isEmpty());
  }

  @Test
  public void testSaveAndDelete() {
    System.out.println(this.adm);
    assertTrue(adminTable.save(this.adm));
    assertTrue(adminTable.delete(this.adm.getId()));
  }

  @Test
  public void testInsertDuplicateKey() {
    assertThrows(
        IllegalStateException.class,
        () -> {
          Admin adm =
              new Admin(
                  "1",
                  Utils.dateToSqlDate(date),
                  "stefano",
                  "furi",
                  35,
                  "stefano.furi7@gmail.com",
                  "C");
          System.out.println(adm);
          assertTrue(adminTable.save(adm));
        });
  }

  @Test
  public void testUpdateValue() {
    var currentAdm = adminTable.findByPrimaryKey("2");
    if (currentAdm.isEmpty()) {
      fail("Admin number 2 is not found");
    }

    var newAdm =
        new Admin(
            "2",
            this.adm.getContractYear(),
            "Massimo",
            "Duri",
            120309120,
            "durissimomassimo@aruba.it",
            "C");

    assertTrue(adminTable.update(newAdm));
    assertTrue(adminTable.update(currentAdm.get()));

    // Testing non esisting primary key update
    var nonExisingAdminKey =
        new Admin(
            "3",
            this.adm.getContractYear(),
            "Massimo",
            "Duri",
            120309120,
            "durissimomassimo@aruba.it",
            "C");

    assertFalse(adminTable.update(nonExisingAdminKey));
  }
}
