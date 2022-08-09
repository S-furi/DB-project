package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.City;

public class CityTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);

  private static final CityTable cityTable = new CityTable(connectionProvider.getMySQLConnection());

  private final City city = new City("F", "T", "F");

  @BeforeAll
  public static void setUp() {
    final var city1 = new City("A", "T", "A");
    final var city2 = new City("C", "E", "F");

    assertTrue(cityTable.save(city1));
    assertTrue(cityTable.save(city2));
  }

  @AfterAll
  public static void tearDown() {
    cityTable.findAll().forEach(t -> cityTable.delete(t.getName()));
  }

  @Test
  void testFindAll() {
    assertFalse(cityTable.findAll().isEmpty());
    assertTrue(cityTable.findAll().size() == 2);
  }

  @Test
  void testFindByPrimaryKey() {
    assertTrue(cityTable.findByPrimaryKey("A").isPresent());
    assertFalse(cityTable.findByPrimaryKey("Z").isPresent());
  }

  @Test
  void testSaveAndDelete() {
    assertTrue(cityTable.save(this.city));
    assertFalse(cityTable.save(this.city));
    assertTrue(cityTable.delete(this.city.getName()));
    assertFalse(cityTable.delete(this.city.getName()));
  }

  @Test
  void testUpdate() {
    var currCity = cityTable.findByPrimaryKey("C");
    if (currCity.isEmpty()) {
      fail("Select Failed!");
    }
    var newCity = new City("C", "Z", "X");

    assertTrue(cityTable.update(newCity));
    assertTrue(cityTable.update(currCity.get()));
  }
}
