package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.RouteInfo;
import db_project.utils.Utils;

public class RouteInfoTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);

  private static final RouteInfoTable routeInfoTable =
      new RouteInfoTable(connectionProvider.getMySQLConnection());

  private final RouteInfo routeInfo =
      new RouteInfo("1", "1", Utils.dateToSqlDate(Utils.buildDate(22, 11, 2011).get()));

  @BeforeAll
  public static void setUp() {
    PathTableTest.setUp();
    TrainTableTest.setUp();

    final var routeInfo1 =
        new RouteInfo("1", "1", Utils.dateToSqlDate(Utils.buildDate(21, 11, 2011).get()));
    final var routeInfo2 =
        new RouteInfo("2", "1", Utils.dateToSqlDate(Utils.buildDate(11, 06, 2011).get()));

    routeInfoTable.save(routeInfo1);
    routeInfoTable.save(routeInfo2);
  }

  @AfterAll
  public static void tearDown() {
    routeInfoTable
        .findAll()
        .forEach(t -> routeInfoTable.delete(List.of(t.getPathId(), t.getTrainId(), t.getDate())));
    TrainTableTest.tearDown();
    PathTableTest.tearDown();
  }

  @Test
  public void testFindAll() {
    assertFalse(routeInfoTable.findAll().isEmpty());
    assertTrue(routeInfoTable.findAll().size() == 2);
  }

  @Test
  public void testFindByPrimaryKey() {
    final List<Object> okKeys =
        List.of("1", "1", Utils.dateToSqlDate(Utils.buildDate(21, 11, 2011).get()));

    // changed date of one day
    final List<Object> failingKeys =
        List.of("1", "1", Utils.dateToSqlDate(Utils.buildDate(22, 11, 2011).get()));

    assertTrue(routeInfoTable.findByPrimaryKey(okKeys).isPresent());
    assertFalse(routeInfoTable.findByPrimaryKey(failingKeys).isPresent());
  }

  @Test
  public void testSaveAndDelete() {
    assertTrue(routeInfoTable.save(this.routeInfo));
    assertFalse(routeInfoTable.save(this.routeInfo));
    assertTrue(
        routeInfoTable.delete(
            List.of(
                this.routeInfo.getPathId(),
                this.routeInfo.getTrainId(),
                this.routeInfo.getDate())));
    assertFalse(
        routeInfoTable.delete(
            List.of(
                this.routeInfo.getPathId(),
                this.routeInfo.getTrainId(),
                this.routeInfo.getDate())));
  }
}
