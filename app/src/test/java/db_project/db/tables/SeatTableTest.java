package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Seat;

public class SeatTableTest {
    private static final String username = "root";
    private static final String password = "123Test123";
    private static final String dbName = "Ferrovia";

    private static final ConnectionProvider connectionProvider = new ConnectionProvider(username, password, dbName);

    private static final SeatTable seatTable = new SeatTable(connectionProvider.getMySQLConnection());

    @BeforeAll
    public static void setUp() {
        CarTableTest.setUp();

        final var seat1 = new Seat("1", "1", 1, 15);
        final var seat2 = new Seat("1", "1", 1, 17);

        assertTrue(seatTable.save(seat1));
        assertTrue(seatTable.save(seat2));
    }

    @AfterAll
    public static void tearDown() {
        seatTable.findAll().forEach(
                t -> seatTable.delete(
                        List.of(t.getClassType(), t.getTrainCode(), t.getCarNumber(), t.getSeatNumber())));
        CarTableTest.tearDown();
    }

    @Test
    public void testFindAll() {
        assertFalse(seatTable.findAll().isEmpty());
        assertTrue(seatTable.findAll().size() == 2);
    }

    @Test
    public void testFindByPrimaryKey() {
        assertTrue(seatTable.findByPrimaryKey(List.of("1", "1", 1, 15)).isPresent());
        assertFalse(seatTable.findByPrimaryKey(List.of("1", "1", 3, 15)).isPresent());
    }
}
