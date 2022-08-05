package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import db_project.db.ConnectionProvider;
import db_project.model.Station;

public class StationTableTest {
    
    private static final String username = "root";
    private static final String password = "123Test123";
    private static final String dbName = "Ferrovia";

    private static final ConnectionProvider connectionProvider = new ConnectionProvider(username, password, dbName);
    private static final StationTable stationTable = new StationTable(connectionProvider.getMySQLConnection());

    @BeforeAll
    static void setUp(){
        final Station station1 = new Station("1", "predappio", 6, "121");
        final Station station2 = new Station("2", "Cesena", 2, "212");

        assertTrue(stationTable.save(station1));
        assertTrue(stationTable.save(station2));
    }

    @AfterAll
    static void tearDown(){
        stationTable.findAll().forEach(t -> stationTable.delete(t.getStationCode()));
    }

    @Test 
    public void testFindByPrimaryKey(){
        assertTrue(stationTable.findByPrimaryKey("1").isPresent());
        assertFalse(stationTable.findByPrimaryKey("3").isPresent());
    }
    
}
