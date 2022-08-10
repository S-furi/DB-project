package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Station;

public class StationTableTest {

  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);
  private static final StationTable stationTable =
      new StationTable(connectionProvider.getMySQLConnection());

  private final Station station3 = new Station("3", "D", 2, "2");
  

  @BeforeAll
  static void setUp() {
    StationManagerTableTest.setUp();
    final Station station1 = new Station("1", "C", 6, "1");
    final Station station2 = new Station("2", "A", 6, "2");

    stationTable.save(station1);
    stationTable.save(station2);
  }

  @AfterAll
  static void tearDown() {
    stationTable.findAll().forEach(t -> stationTable.delete(t.getStationCode()));
    StationManagerTableTest.tearDown();
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(stationTable.findByPrimaryKey("1").isPresent());
    assertFalse(stationTable.findByPrimaryKey("5").isPresent());
  }

  @Test
  public void testUpdate(){
    final var currStation = stationTable.findByPrimaryKey("1");
    if(currStation.isEmpty()){
      fail("Select Failed");
    }
    assertTrue(stationTable.update(new Station("1", this.station3.getStationName(), this.station3.getRails(), this.station3.getManagerCode())));
  }
}
