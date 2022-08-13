package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import db_project.db.ConnectionProvider;
import db_project.model.Car;

public class CarTableTest {
    private static final String username = "root";
    private static final String password = "123Test123";
    private static final String dbName = "Ferrovia";

    private static final ConnectionProvider connectionProvider = 
        new ConnectionProvider(username, password, dbName);
    private static final CarTable carTable = 
        new CarTable(connectionProvider.getMySQLConnection());
    
    @BeforeAll
    public static void setUp() {
        carClassTableTest.setUp();
        TrainTableTest.setUp();
        
        final Car car1 = new Car("1", "1", 1, 50, false);
        final Car car2 = new Car("1", "1", 2, 25, true);

        assertTrue(carTable.save(car1));
        assertTrue(carTable.save(car2));
    }

    @AfterAll
    public static void tearDown() {
        carTable.findAll().forEach(t -> carTable.delete(List.of(t.getClassType(), t.getTrainCode(), t.getPosition())));
        TrainTableTest.tearDown();
        carClassTableTest.tearDown();
    }

    @Test
    public void findAll() {
        assertFalse(carTable.findAll().isEmpty());
        assertTrue(carTable.findAll().size() == 2);
    }

    @Test
    public void testFindByPrimaryKey() {
        assertTrue(carTable.findByPrimaryKey(List.of("1", "1", 1)).isPresent());
        assertTrue(carTable.findByPrimaryKey(List.of("1", "1", 2)).isPresent());
        assertFalse(carTable.findByPrimaryKey(List.of("1", "1", 3)).isPresent());       
    }
}
