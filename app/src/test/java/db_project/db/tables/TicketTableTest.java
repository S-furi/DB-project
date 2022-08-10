package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Ticket;
import db_project.utils.Utils;

public class TicketTableTest {
    private static final String username = "root";
    private static final String password = "123Test123";
    private static final String dbName = "Ferrovia";

    private static final ConnectionProvider connectionProvider = new ConnectionProvider(username, password, dbName);

    private static final TicketTable ticketTable = new TicketTable(connectionProvider.getMySQLConnection());

    private final Ticket ticket = new Ticket("3", true, Optional.of("2"), "2", "2", "1",
        Utils.dateToSqlDate(Utils.buildDate(11, 06, 2011).get()));

    @BeforeAll
    public static void setUp() {
        GroupTableTest.setUp();
        PassengerTableTest.setUp();
        RouteInfoTableTest.setUp();

        final var ticket1 = new Ticket(
                "1",
                false,
                Optional.empty(),
                "1",
                "1",
                "1",
                Utils.dateToSqlDate(Utils.buildDate(21, 11, 2011).get()));
        
        
        
        final var ticket2 = new Ticket(
                "2",
                true,
                Optional.of("1"),
                "2",
                "2",
                "1",
                Utils.dateToSqlDate(Utils.buildDate(11, 06, 2011).get()));

        ticketTable.save(ticket1);
        ticketTable.save(ticket2);
    }

    @AfterAll
    public static void tearDown() {
        ticketTable.findAll().forEach(t -> ticketTable.delete(t.getTicketId()));
        TrainTableTest.tearDown();
        PathTableTest.tearDown();
        PassengerTableTest.tearDown();
        GroupTableTest.tearDown();
    }

    @Test
    public void testFindAll() {
        assertFalse(ticketTable.findAll().isEmpty());
        assertTrue(ticketTable.findAll().size() == 2);
    }

    @Test
    public void testFindByPrimaryKey() {
        assertTrue(ticketTable.findByPrimaryKey("1").isPresent());
        assertFalse(ticketTable.findByPrimaryKey("9").isPresent());
    }

    @Test
    public void testSaveAndDelete() {
        assertTrue(ticketTable.save(this.ticket));
        assertFalse(ticketTable.save(this.ticket));
        assertTrue(ticketTable.delete(this.ticket.getTicketId()));
        assertFalse(ticketTable.delete(this.ticket.getTicketId()));
    }

    @Test
    public void testUpdate() {
        final var currTicket = ticketTable.findByPrimaryKey("1");
        if (currTicket.isEmpty()) {
            fail("Select Failed");
        }
        final var newTicket = new Ticket(
            "1", 
            true, 
            this.ticket.getGroupId(), 
            this.ticket.getPassengerId(), 
            this.ticket.getPathId(), 
            this.ticket.getTrainId(), 
            this.ticket.getDate());
        
        assertTrue(ticketTable.update(newTicket));
        assertTrue(ticketTable.update(currTicket.get()));
    }

}
