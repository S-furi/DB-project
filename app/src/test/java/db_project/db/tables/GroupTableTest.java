package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Group;

public class GroupTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);

  private final GroupTable groupTable =
      new GroupTable(this.connectionProvider.getMySQLConnection());

  private final Group group = new Group("3", 13);

  @Test
  public void testFindAll() {
    assertFalse(this.groupTable.findAll().isEmpty());
    assertTrue(this.groupTable.findAll().size() == 2);
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(this.groupTable.findByPrimaryKey("1").isPresent());
    assertFalse(this.groupTable.findByPrimaryKey("8").isPresent());
  }

  @Test
  public void testSaveAndDelete() {
    assertTrue(this.groupTable.save(group));
    assertThrows(
        IllegalStateException.class,
        () -> {
          this.groupTable.save(group);
        });
    assertTrue(this.groupTable.delete(this.group.getGroupId()));
    assertFalse(this.groupTable.delete(this.group.getGroupId()));
  }

  @Test
  public void testUpdate() {
    var currGroup = this.groupTable.findByPrimaryKey("1");
    if (currGroup.isEmpty()) {
      fail("Select Failed!");
    }
    var newGroup = new Group("1", 12);

    assertTrue(this.groupTable.update(newGroup));
    assertTrue(this.groupTable.update(currGroup.get()));
  }
}
