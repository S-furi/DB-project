package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Group;

public class GroupTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);

  private static final GroupTable groupTable =
      new GroupTable(connectionProvider.getMySQLConnection());

  private final Group group = new Group("3", 13);

  @BeforeAll
  static void setUp() {
    var group1 = new Group("1", 9);
    var group2 = new Group("2", 5);

    assertTrue(groupTable.save(group1));
    assertTrue(groupTable.save(group2));
  }

  @AfterAll
  static void tearDown() {
    groupTable.findAll().forEach(t -> groupTable.delete(t.getGroupId()));
  }

  @Test
  public void testFindAll() {
    assertFalse(groupTable.findAll().isEmpty());
    assertTrue(groupTable.findAll().size() == 2);
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(groupTable.findByPrimaryKey("1").isPresent());
    assertFalse(groupTable.findByPrimaryKey("8").isPresent());
  }

  @Test
  public void testSaveAndDelete() {
    assertTrue(groupTable.save(group));
    assertThrows(
        IllegalStateException.class,
        () -> {
          groupTable.save(group);
        });
    assertTrue(groupTable.delete(this.group.getGroupId()));
    assertFalse(groupTable.delete(this.group.getGroupId()));
  }

  @Test
  public void testUpdate() {
    var currGroup = groupTable.findByPrimaryKey("1");
    if (currGroup.isEmpty()) {
      fail("Select Failed!");
    }
    var newGroup = new Group("1", 12);

    assertTrue(groupTable.update(newGroup));
    assertTrue(groupTable.update(currGroup.get()));
  }
}
