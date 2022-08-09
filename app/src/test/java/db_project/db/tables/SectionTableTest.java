package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Section;

public class SectionTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);

  private static final SectionTable sectionTable =
      new SectionTable(connectionProvider.getMySQLConnection());

  private final Section section = new Section("1", "2", "3", 50);

  @BeforeAll
  public static void setUp() {
    StationTableTest.setUp();
    final var section1 = new Section("1", "2", "1", 50);
    final var section2 = new Section("2", "1", "2", 50);

    assertTrue(sectionTable.save(section1));
    assertTrue(sectionTable.save(section2));
  }

  @AfterAll
  public static void tearDown() {
    sectionTable.findAll().forEach(t -> sectionTable.delete(t.getSectionCode()));
    StationTableTest.tearDown();
  }

  @Test
  public void testFindAll() {
    assertFalse(sectionTable.findAll().isEmpty());
    assertTrue(sectionTable.findAll().size() == 2);
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(sectionTable.findByPrimaryKey("1").isPresent());
    assertFalse(sectionTable.findByPrimaryKey("9").isPresent());
  }

  @Test
  public void testSaveAndDelete() {
    assertTrue(sectionTable.save(this.section));
    assertThrows(
        IllegalStateException.class,
        () -> {
          sectionTable.save(this.section);
        });
    assertTrue(sectionTable.delete(this.section.getSectionCode()));
    assertFalse(sectionTable.delete(this.section.getSectionCode()));
  }

  @Test
  public void testUpdate() {
    final var currSection = sectionTable.findByPrimaryKey("1");
    if (currSection.isEmpty()) {
      fail("Select failed");
    }
    final var newSection =
        new Section(
            this.section.getStartStation(),
            this.section.getEndStation(),
            "1",
            this.section.getDistance());

    assertTrue(sectionTable.update(newSection));
    assertTrue(sectionTable.update(currSection.get()));
  }
}
