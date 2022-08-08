package db_project.db.tables;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import db_project.db.ConnectionProvider;
import db_project.model.StationManager;

public class StationManagerTableTest {

  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);
  private static final StationManagerTable stationManagerTable =
      new StationManagerTable(connectionProvider.getMySQLConnection());

  private static final Date date = new Date(12122001);
  private final StationManager stationManager =
      new StationManager("3", date, "Peppe", "Pesce", "3", "peppe@peppe.com", "A");

  @BeforeAll
  static void setUp() {

    Date date1 = new Date(12012001);
    Date date2 = new Date(12022001);

    final StationManager stationManager1 =
        new StationManager("1", date1, "Mimmo", "Baresi", "3", "a@gmail.com", "CastelFranco");
    final StationManager stationManager2 =
        new StationManager("2", date2, "Franco", "Pino", "2", "franco@pino.com", "CastelFranco");
    System.out.println("DIODID");
    assertTrue(stationManagerTable.save(stationManager1));
    assertTrue(stationManagerTable.save(stationManager2));
  }

  @AfterAll
  static void tearDown() {
    stationManagerTable.findAll().forEach(t -> stationManagerTable.delete(t.getManagerCode()));
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(stationManagerTable.findByPrimaryKey("1").isPresent());
    assertFalse(stationManagerTable.findByPrimaryKey("2").isPresent());
  }

  @Test
  public void testSaveAndDelete() {
    assertTrue(stationManagerTable.save(this.stationManager));
    assertThrows(
        IllegalStateException.class,
        () -> {
          stationManagerTable.save(this.stationManager);
        });
    assertTrue(stationManagerTable.delete(this.stationManager.getManagerCode()));
    assertFalse(stationManagerTable.delete(this.stationManager.getManagerCode()));
  }
}
