package db_project.db.tables;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

  private CarClass carClass3 = new CarClass(3, 50);

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);
  private static final CarClassTable carClassTable =
      new CarClassTable(connectionProvider.getMySQLConnection());

  @BeforeAll
  static void setUp() {
    final CarClass carclass1 = new CarClass(1, 10);
    final CarClass carclass2 = new CarClass(2, 15);

    assertTrue(carClassTable.save(carclass1));
    assertTrue(carClassTable.save(carclass2));
  }

  @AfterAll
  static void tearDown() {
    carClassTable.findAll().forEach(t -> carClassTable.delete(t.getClassType()));
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(carClassTable.findByPrimaryKey(1).isPresent());
    assertFalse(carClassTable.findByPrimaryKey(4).isPresent());
  }

  @Test
  public void testFindAll(){
    assertFalse(carClassTable.findAll().isEmpty());
    assertTrue(carClassTable.findAll().size() == 2);
  }

  @Test
  public void testSaveAndDelete() {
    assertTrue(carClassTable.save(this.carClass3));
    assertThrows(
        IllegalStateException.class,
        () -> {
          carClassTable.save(this.carClass3);
        });
    assertTrue(carClassTable.delete(this.carClass3.getClassType()));
    assertFalse(carClassTable.delete(this.carClass3.getClassType()));
  }

  @Test
  public void testUpdate() {
    final var curCarClass = carClassTable.findByPrimaryKey(1);
    if (curCarClass.isEmpty()) {
      fail("Select Failed");
    }
    assertTrue(carClassTable.update(new CarClass(1, 100)));
    assertTrue(carClassTable.update(curCarClass.get()));
  }
}
