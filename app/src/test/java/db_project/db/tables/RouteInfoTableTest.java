package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import db_project.db.ConnectionProvider;
import db_project.db.tables.RouteInfoTable;
import db_project.model.RouteInfo;

public class RouteInfoTableTest {
    private static final String username = "root";
    private static final String password = "123Test123";
    private static final String dbName = "Ferrovia";

    private static final ConnectionProvider connectionProvider =
        new ConnectionProvider(username, password, dbName);
    
    private static final RouteInfoTable routeInforTable = 
        new RouteInfoTable(connectionProvider.getMySQLConnection());

    @BeforeAll
    static void setUp() {
    }

    @AfterAll
    static void tearDown() {
    }
    
    @Test
    public void testFindAll() {
    }

    @Test
    public void testFindByPrimaryKey() {

    }

    @Test
    public void testSave() {

    }

    @Test
    public void testUpdate() {

    }
}
