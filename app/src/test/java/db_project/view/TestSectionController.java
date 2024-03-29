package db_project.view;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import db_project.db.dbGenerator.DBGenerator;
import db_project.db.tables.PathTable;
import db_project.model.Path;
import db_project.view.adminPanelController.PathController;
import db_project.view.adminPanelController.SectionController;

public class TestSectionController {
  private static final DBGenerator dbGenerator = new DBGenerator();
  private SectionController sectionController;
  private PathController pathController;
  private PathTable pathTable;

  @AfterAll
  public static void tearDown() {
    dbGenerator.dropDB();
  }

  @Test
  public void testSectionController() {
    dbGenerator.createDB();
    dbGenerator.createTables();
    dbGenerator.populateTables();
    this.pathController = new PathController(dbGenerator);
    this.sectionController = new SectionController(dbGenerator, this.pathController);
    this.pathTable = (PathTable) dbGenerator.getTableByClass(PathTable.class);
    final Optional<Path> routePath = this.pathTable.findByPrimaryKey("CA-LA");
    if (routePath.isEmpty()) {
      fail("primary key not found");
    }
    this.sectionController.computeSectionsFromPath(routePath.get());
    final var solution = this.sectionController.getPathDetails();
    assertFalse(solution.isEmpty());
    solution.forEach(System.out::println);
  }
}
