package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

import db_project.db.ConnectionProvider;

public class AdminTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);
      
    private final AdminTable adminTable = new AdminTable(connectionProvider.getMySQLConnection());
    
    @Test
    public void testFindByPrimaryKey() {
        assertTrue(this.adminTable.findByPrimaryKey(1).isPresent());
    }

    @Test
    public void testFindAll() {
        assertFalse (this.adminTable.findAll().isEmpty());
    }
}
