package db_project.db.dataLoading;

// import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import db_project.db.dbGenerator.DBGenerator;
import db_project.db.tables.CityTable;

import org.junit.jupiter.api.BeforeAll;

public class DataLoadingTest {
  private static final DBGenerator dbGenerator = new DBGenerator();

  @BeforeAll
  public static void setUp() {
    dbGenerator.createDB();
    dbGenerator.createTables();
  }

  @AfterAll
  public static void tearDown() {
    dbGenerator.dropDB();
  }

  @Test
  public void testCityReadAndInsertion() {
    final var optCityTable =
        dbGenerator.getTables().stream()
            .filter(t -> t.getClass().equals(CityTable.class))
            .findAny();
    assertTrue(optCityTable.isPresent());
    final CityTable cityTable = (CityTable) optCityTable.get();
    final var cities = cityTable.readFromFile();
    cities.forEach(t -> assertTrue(cityTable.save(t)));
  }
}
