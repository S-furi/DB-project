package db_project.db.dataLoading;

// import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import db_project.db.dbGenerator.DBGenerator;
import db_project.db.tables.AdminTable;
import db_project.db.tables.CarClassTable;
import db_project.db.tables.CityTable;
import db_project.db.tables.DriverTable;
import db_project.db.tables.GroupTable;
import db_project.db.tables.LoyaltyCardTable;
import db_project.db.tables.PassengerTable;
import db_project.db.tables.PathInfoTable;
import db_project.db.tables.PathTable;
import db_project.db.tables.SectionTable;
import db_project.db.tables.StationManagerTable;
import db_project.db.tables.StationTable;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

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

  @BeforeEach
  public void clean() {
    dbGenerator.dropDB();
    dbGenerator.createDB();
    dbGenerator.createTables();
  }

  private void createCitiesDependency() {
    final CityTable cityTable = (CityTable) dbGenerator.getTableByClass(CityTable.class);
    final var cities = cityTable.readFromFile();
    cities.forEach(t -> cityTable.save(t));
  }

  @Test
  public void testCityReadAndStored() {
    final CityTable cityTable = (CityTable) dbGenerator.getTableByClass(CityTable.class);
    final var cities = cityTable.readFromFile();
    cities.forEach(t -> assertTrue(cityTable.save(t)));
    assertTrue(cityTable.findAll().size() == cities.size());
    cityTable.findAll().forEach(t -> assertTrue(cityTable.readFromFile().contains(t)));
  }

  private void createAdminDepencency() {
    this.createCitiesDependency();
    final AdminTable adminTable = (AdminTable) dbGenerator.getTableByClass(AdminTable.class);
    final var admins = adminTable.readFromFile();
    admins.forEach(t -> adminTable.save(t));
  }

  @Test
  public void testAdminReadAndInsertion() {
    this.createCitiesDependency();
    final AdminTable adminTable = (AdminTable) dbGenerator.getTableByClass(AdminTable.class);
    final var admins = adminTable.readFromFile();
    admins.forEach(t -> assertTrue(adminTable.save(t)));
    assertTrue(adminTable.findAll().size() == admins.size());
  }

  @Test
  public void testDriverReadAndInsertion() {
    this.createCitiesDependency();
    final DriverTable driverTable = (DriverTable) dbGenerator.getTableByClass(DriverTable.class);
    final var drivers = driverTable.readFromFile();
    drivers.forEach(t -> assertTrue(driverTable.save(t)));
    assertTrue(driverTable.findAll().size() == drivers.size());
  }

  private void createManagerDepencency() {
    this.createCitiesDependency();
    final StationManagerTable stationManagerTable =
        (StationManagerTable) dbGenerator.getTableByClass(StationManagerTable.class);
    final var managers = stationManagerTable.readFromFile();
    managers.forEach(t -> stationManagerTable.save(t));
  }

  @Test
  public void testStationManagersReadAndInsertion() {
    this.createCitiesDependency();
    final StationManagerTable stationManagerTable =
        (StationManagerTable) dbGenerator.getTableByClass(StationManagerTable.class);
    final var managers = stationManagerTable.readFromFile();
    managers.forEach(t -> assertTrue(stationManagerTable.save(t)));
    assertTrue(stationManagerTable.findAll().size() == managers.size());
  }

  @Test
  public void testPassengerReadAndInsertion() {
    this.createCitiesDependency();
    final PassengerTable passengerTable =
        (PassengerTable) dbGenerator.getTableByClass(PassengerTable.class);
    final var passengers = passengerTable.readFromFile();
    passengers.forEach(t -> assertTrue(passengerTable.save(t)));
    assertTrue(passengerTable.findAll().size() == passengers.size());
  }

  private void createStationsDependencies() {
    this.createManagerDepencency();
    final StationTable stationTable =
        (StationTable) dbGenerator.getTableByClass(StationTable.class);
    stationTable.readFromFile().forEach(t -> stationTable.save(t));
  }

  @Test
  public void testStationReadAndInsertion() {
    this.createManagerDepencency();
    final StationTable stationTable =
        (StationTable) dbGenerator.getTableByClass(StationTable.class);
    final var stations = stationTable.readFromFile();
    stations.forEach(t -> stationTable.save(t));
    stationTable.findAll().forEach(t -> assertTrue(stations.contains(t)));
  }

  @Test
  public void testGroupsReadAndInsertion() {
    final GroupTable groupTable = (GroupTable) dbGenerator.getTableByClass(GroupTable.class);
    final var groups = groupTable.readFromFile();
    groups.forEach(t -> assertTrue(groupTable.save(t)));
    groupTable.findAll().forEach(t -> assertTrue(groups.contains(t)));
  }

  @Test
  public void testLoyaltyCardsReadAndInsertion() {
    final LoyaltyCardTable loyaltyCardTable =
        (LoyaltyCardTable) dbGenerator.getTableByClass(LoyaltyCardTable.class);
    final var loyaltyCards = loyaltyCardTable.readFromFile();
    loyaltyCards.forEach(t -> assertTrue(loyaltyCardTable.save(t)));
    loyaltyCardTable.findAll().forEach(t -> assertTrue(loyaltyCards.contains(t)));
  }

  @Test
  public void testCarClassReadAndInsertion() {
    final CarClassTable carClassTable =
        (CarClassTable) dbGenerator.getTableByClass(CarClassTable.class);
    final var carClasses = carClassTable.readFromFile();
    carClasses.forEach(t -> assertTrue(carClassTable.save(t)));
    carClassTable.findAll().forEach(t -> assertTrue(carClasses.contains(t)));
  }

  private void createPathDependencies() {
    this.createAdminDepencency();
    final PathTable pathTable = (PathTable) dbGenerator.getTableByClass(PathTable.class);
    pathTable.readFromFile().forEach(t -> assertTrue(pathTable.save(t)));
  }

  @Test
  public void testPathReadAndInsertion() {
    this.createAdminDepencency();
    final PathTable pathTable = (PathTable) dbGenerator.getTableByClass(PathTable.class);
    final var paths = pathTable.readFromFile();
    paths.forEach(t -> assertTrue(pathTable.save(t)));
    pathTable.findAll().forEach(t -> assertTrue(paths.contains(t)));
  }

  private void createSectionsDependencies() {
    this.createStationsDependencies();
    final SectionTable sectionTable =
        (SectionTable) dbGenerator.getTableByClass(SectionTable.class);
    sectionTable.readFromFile().forEach(t -> sectionTable.save(t));
  }

  @Test
  public void testSectionReadAndInsertion() {
    this.createStationsDependencies();
    final SectionTable sectionTable =
        (SectionTable) dbGenerator.getTableByClass(SectionTable.class);
    final var tables = sectionTable.readFromFile();
    tables.forEach(t -> assertTrue(sectionTable.save(t)));
    sectionTable.findAll().forEach(t -> assertTrue(tables.contains(t)));
  }

  @Test
  public void testPathInfoReadAndInsertion() {
    this.createSectionsDependencies();
    this.createPathDependencies();
    final PathInfoTable pathInfoTable =
        (PathInfoTable) dbGenerator.getTableByClass(PathInfoTable.class);
    final var tables = pathInfoTable.readFromFile();
    tables.forEach(
        t -> {
          if (!pathInfoTable.save(t)) {
            System.out.println("errore con " + t.toString());
          } else {
            System.out.println("TUTTO A POSTO CON " + t.toString());
          }
        });
    pathInfoTable.findAll().forEach(t -> assertTrue(tables.contains(t)));
  }
}
