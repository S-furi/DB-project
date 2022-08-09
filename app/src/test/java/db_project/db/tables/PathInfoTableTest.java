package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.PathInfo;

public class PathInfoTableTest {
    private static final String username = "root";
    private static final String password = "123Test123";
    private static final String dbName = "Ferrovia";

    private static final ConnectionProvider connectionProvider = 
        new ConnectionProvider(username, password, dbName);

    private static final PathInfoTable pathInfoTable = 
        new PathInfoTable(connectionProvider.getMySQLConnection());

    @BeforeAll
    public static void setUp() {
        SectionTableTest.setUp();
        PathTableTest.setUp();

        final var pathInfo1 = new PathInfo("1", 1, "1");
        final var pathInfo2 = new PathInfo("2", 2, "1");

        assertTrue(pathInfoTable.save(pathInfo1));
        assertTrue(pathInfoTable.save(pathInfo2));
    }

    @AfterAll
    public static void tearDown() {
        pathInfoTable.findAll().forEach(t -> pathInfoTable.delete(t.getSectionId()));
        PathTableTest.tearDown();
        SectionTableTest.tearDown();
    }

    @Test
    public void testFindAll() {
        assertFalse(pathInfoTable.findAll().isEmpty());
    }
}
