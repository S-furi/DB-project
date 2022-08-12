package db_project.db.dataLoading;

// import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import db_project.db.dbGenerator.DBGenerator;
import db_project.db.tables.AdminTable;
import db_project.db.tables.CityTable;
import db_project.db.tables.DriverTable;
import db_project.db.tables.StationManagerTable;

import org.junit.jupiter.api.BeforeAll;

public class DataLoadingTest {
  private static final DBGenerator dbGenerator = new DBGenerator();

  @BeforeAll
  public static void setUp() {
    dbGenerator.createDB();
    dbGenerator.createTables();
    createCitiesDependency();
  }
  
  private static void createCitiesDependency() {
    final CityTable cityTable = (CityTable) dbGenerator.getTableByClass(CityTable.class);
    final var cities = cityTable.readFromFile();
    cities.forEach(t -> cityTable.save(t));
  }

  @AfterAll
  public static void tearDown() {
    dbGenerator.dropDB();
  }

  @Test
  public void testCityReadAndStored() {
    final CityTable cityTable = (CityTable) dbGenerator.getTableByClass(CityTable.class);
    assertTrue(cityTable.findAll().size() == cityTable.readFromFile().size());
  }


  @Test
  public void testAdminReadAndInsertion() {
    final AdminTable adminTable = (AdminTable) dbGenerator.getTableByClass(AdminTable.class);
    final var admins = adminTable.readFromFile();
    admins.forEach(t -> assertTrue(adminTable.save(t)));
    assertTrue(adminTable.findAll().size() == admins.size());
  }

  @Test
  public void testDriverReadAndInsertion() {
    final DriverTable driverTable = (DriverTable) dbGenerator.getTableByClass(DriverTable.class);
    final var drivers = driverTable.readFromFile();
    drivers.forEach(t -> assertTrue(driverTable.save(t)));
    assertTrue(driverTable.findAll().size() == drivers.size());
  }

  @Test
  public void testStationManagersReadAndInsertion() {
    final StationManagerTable stationManagerTable = (StationManagerTable) dbGenerator.getTableByClass(StationManagerTable.class);
    final var managers = stationManagerTable.readFromFile();
    managers.forEach(t -> assertTrue(stationManagerTable.save(t)));
    assertTrue(stationManagerTable.findAll().size() == managers.size());
  }
}
