package db_project.db;

// import static org.junit.Assert.fail;
// import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

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
import db_project.db.tables.RouteInfoTable;
import db_project.db.tables.SectionTable;
import db_project.db.tables.StationManagerTable;
import db_project.db.tables.StationTable;
import db_project.db.tables.TicketDetailTable;
import db_project.db.tables.TicketTable;
import db_project.db.tables.TrainTable;

public class TestDBGenerator {
  static final ConnectionProvider connection =
      new ConnectionProvider("root", "123Test123", "Railway");
  private final AdminTable adminTable = new AdminTable(connection.getMySQLConnection());
  private final CityTable cityTable = new CityTable(connection.getMySQLConnection());
  private final CarClassTable carClassTable = new CarClassTable(connection.getMySQLConnection());
  private final TicketTable ticketTable = new TicketTable(connection.getMySQLConnection());
  private final GroupTable groupTable = new GroupTable(connection.getMySQLConnection());
  private final TicketDetailTable ticketDetailTable =
      new TicketDetailTable(connection.getMySQLConnection());
  private final PathInfoTable pathInfoTable = new PathInfoTable(connection.getMySQLConnection());
  private final LoyaltyCardTable loyaltyCardTable =
      new LoyaltyCardTable(connection.getMySQLConnection());
  private final DriverTable driverTable = new DriverTable(connection.getMySQLConnection());
  private final PassengerTable passengerTable = new PassengerTable(connection.getMySQLConnection());
  private final RouteInfoTable routeInfoTable = new RouteInfoTable(connection.getMySQLConnection());
  private final PathTable pathTable = new PathTable(connection.getMySQLConnection());
  private final StationManagerTable stationManagerTable =
      new StationManagerTable(connection.getMySQLConnection());
  private final StationTable stationTable = new StationTable(connection.getMySQLConnection());
  private final SectionTable sectionTable = new SectionTable(connection.getMySQLConnection());
  private final TrainTable trainTable = new TrainTable(connection.getMySQLConnection());

  @BeforeAll
  public static void setUp() {
    DBGenerator.createDB();
  }

  @Test
  public void testAdminTable() {
    assertTrue(adminTable.createTable());
    assertTrue(adminTable.isCreated());
    assertTrue(adminTable.dropTable());
  }

  @Test
  public void testCityTable() {
    assertTrue(cityTable.createTable());
    assertTrue(cityTable.isCreated());
    assertTrue(cityTable.dropTable());
  }

  @Test
  public void testCarClassTable() {
    assertTrue(carClassTable.createTable());
    assertTrue(carClassTable.isCreated());
    assertTrue(carClassTable.dropTable());
  }

  @Test
  public void testTicketTable() {
    assertTrue(ticketTable.createTable());
    assertTrue(ticketTable.isCreated());
    assertTrue(ticketTable.dropTable());
  }

  @Test
  public void testGroupTable() {
    assertTrue(groupTable.createTable());
    assertTrue(groupTable.isCreated());
    assertTrue(groupTable.dropTable());
  }

  @Test
  public void testTicketDetailTable() {
    assertTrue(ticketDetailTable.createTable());
    assertTrue(ticketDetailTable.isCreated());
    assertTrue(ticketDetailTable.dropTable());
  }

  @Test
  public void testPathInfoTable() {
    assertTrue(pathInfoTable.createTable());
    assertTrue(pathInfoTable.isCreated());
    assertTrue(pathInfoTable.dropTable());
  }

  @Test
  public void testLoyaltyCardTable() {
    assertTrue(loyaltyCardTable.createTable());
    assertTrue(loyaltyCardTable.isCreated());
    assertTrue(loyaltyCardTable.dropTable());
  }

  @Test
  public void testDriverTable() {
    assertTrue(driverTable.createTable());
    assertTrue(driverTable.isCreated());
    assertTrue(driverTable.dropTable());
  }

  @Test
  public void testPassengerTable() {
    assertTrue(passengerTable.createTable());
    assertTrue(passengerTable.isCreated());
    assertTrue(passengerTable.dropTable());
  }

  @Test
  public void testRouteInfoTable() {
    assertTrue(routeInfoTable.createTable());
    assertTrue(routeInfoTable.isCreated());
    assertTrue(routeInfoTable.dropTable());
  }

  @Test
  public void testPathTable() {
    assertTrue(pathTable.createTable());
    assertTrue(pathTable.isCreated());
    assertTrue(pathTable.dropTable());
  }

  @Test
  public void testStationManagerTable() {
    assertTrue(stationManagerTable.createTable());
    assertTrue(stationManagerTable.isCreated());
    assertTrue(stationManagerTable.dropTable());
  }

  @Test
  public void testStationTable() {
    assertTrue(stationTable.createTable());
    assertTrue(stationTable.isCreated());
    assertTrue(stationTable.dropTable());
  }

  @Test
  public void testSectionTable() {
    assertTrue(sectionTable.createTable());
    assertTrue(sectionTable.isCreated());
    assertTrue(sectionTable.dropTable());
  }

  @Test
  public void testTrainTable() {
    assertTrue(trainTable.createTable());
    assertTrue(trainTable.isCreated());
    assertTrue(trainTable.dropTable());
  }
}
