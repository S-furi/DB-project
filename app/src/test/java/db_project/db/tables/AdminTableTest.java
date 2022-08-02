package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Admin;
import db_project.utils.Utils;

public class AdminTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);

  private final AdminTable adminTable = new AdminTable(connectionProvider.getMySQLConnection());

  private final Date date = Utils.buildDate(12, 12, 2012).get();
  private final Admin adm =
      new Admin("3", date, "stefano", "furi", 35, "stefano.furi7@gmail.com", "C");

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(this.adminTable.findByPrimaryKey("1").isPresent());
    assertFalse(this.adminTable.findByPrimaryKey("9").isPresent());
  }

  @Test
  public void testFindAll() {
    assertFalse(this.adminTable.findAll().isEmpty());
  }

  @Test
  public void testSaveAndDelete() {
    System.out.println(this.adm);
    assertTrue(this.adminTable.save(this.adm));
    assertTrue(this.adminTable.delete(this.adm.getId()));
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
          assertTrue(this.adminTable.save(adm));
        });
  }
}
