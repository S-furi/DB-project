package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Path;

public class PathTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);

  private static final PathTable pathTable = new PathTable(connectionProvider.getMySQLConnection());

  private final Path path = new Path("3", "10:10", 10, "1");

  @BeforeAll
  static void setUp() {
    AdminTableTest.setUp();
    final var path1 = new Path("1", "01:11", 5, "1");
    final var path2 = new Path("2", "02:21", 2, "2");

    assertTrue(pathTable.save(path1));
    assertTrue(pathTable.save(path2));
  }

  @AfterAll
  static void tearDown() {
    pathTable.findAll().forEach(t -> pathTable.delete(t.getPathCode()));
    AdminTableTest.tearDown();
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(pathTable.findByPrimaryKey("1").isPresent());
    assertFalse(pathTable.findByPrimaryKey("9").isPresent());
  }

  @Test
  public void testFindAll() {
    assertFalse(pathTable.findAll().isEmpty());
    assertTrue(pathTable.findAll().size() == 2);
  }

  @Test
  public void testSaveAndDelete() {
    assertTrue(pathTable.save(this.path));
    assertFalse(pathTable.save(this.path));
    assertTrue(pathTable.delete(this.path.getPathCode()));
    assertFalse(pathTable.delete(this.path.getPathCode()));
  }

  @Test
  public void testUpdateValue() {
    var currPath = pathTable.findByPrimaryKey("1");
    if (currPath.isEmpty()) {
      fail("Select failed!");
    }
    var newPath =
        new Path("1", this.path.getTotalTime(), this.path.getStops(), this.path.getAdminID());
    assertTrue(pathTable.update(newPath));
    assertTrue(pathTable.update(currPath.get()));
  }
}
