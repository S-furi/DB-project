package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.TicketDetail;
import db_project.utils.Utils;

public class TicketDetailTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);

  private static final TicketDetailTable ticketDetailTable =
      new TicketDetailTable(connectionProvider.getMySQLConnection());

  @BeforeAll
  public static void setUp() {
    TicketTableTest.setUp();
    SeatTableTest.setUp();

    final var ticketDetail1 =
        new TicketDetail(
            "1", Utils.dateToSqlDate(Utils.buildDate(11, 11, 1911).get()), "1", "1", 1, 15);
    final var ticketDetail2 =
        new TicketDetail(
            "2", Utils.dateToSqlDate(Utils.buildDate(12, 11, 1911).get()), "1", "1", 1, 15);

    ticketDetailTable.save(ticketDetail1);
    ticketDetailTable.save(ticketDetail2);
  }

  @AfterAll
  public static void tearDown() {
    ticketDetailTable
        .findAll()
        .forEach(
            t ->
                ticketDetailTable.delete(
                    List.of(
                        t.getTrainClass(),
                        t.getTrainId(),
                        t.getCarNumber(),
                        t.getSeatNumber(),
                        t.getTripDate())));
    TicketTableTest.tearDown();
    SeatTableTest.tearDown();
  }

  @Test
  public void findAll() {
    assertFalse(ticketDetailTable.findAll().isEmpty());
    assertTrue(ticketDetailTable.findAll().size() == 2);
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(
        ticketDetailTable
            .findByPrimaryKey(
                List.of("1", "1", 1, 15, Utils.dateToSqlDate(Utils.buildDate(12, 11, 1911).get())))
            .isPresent());
    assertFalse(
        ticketDetailTable
            .findByPrimaryKey(
                List.of("1", "1", 1, 17, Utils.dateToSqlDate(Utils.buildDate(12, 11, 1911).get())))
            .isPresent());
  }
}
