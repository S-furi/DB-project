package db_project.db.tables;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import db_project.db.ConnectionProvider;
import db_project.model.CarClass;

public class carClassTableTest {

  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private final CarClass carClass = new CarClass("2", 15);

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);
  private static final CarClassTable carClassTable =
      new CarClassTable(connectionProvider.getMySQLConnection());

  @BeforeAll
  static void setUp() {
    final CarClass carclass1 = new CarClass("1", 10);

    carClassTable.save(carclass1);
  }

  @AfterAll
  static void tearDown() {
    carClassTable.findAll().forEach(t -> carClassTable.delete(t.getClassType()));
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(carClassTable.findByPrimaryKey("1").isPresent());
    assertFalse(carClassTable.findByPrimaryKey("4").isPresent());
  }

  @Test
  public void testFindAll() {
    assertFalse(carClassTable.findAll().isEmpty());
    assertTrue(carClassTable.findAll().size() == 1);
  }

  @Test
  public void testSaveAndDelete() {
    assertTrue(carClassTable.save(this.carClass));
    assertFalse(carClassTable.save(this.carClass));
    assertTrue(carClassTable.delete(this.carClass.getClassType()));
    assertFalse(carClassTable.delete(this.carClass.getClassType()));
  }

  @Test
  public void testUpdate() {
    final var curCarClass = carClassTable.findByPrimaryKey("1");
    if (curCarClass.isEmpty()) {
      fail("Select Failed");
    }
    assertTrue(carClassTable.update(new CarClass("1", 100)));
    assertTrue(carClassTable.update(curCarClass.get()));
  }
}
