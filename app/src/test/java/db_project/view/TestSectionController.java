package db_project.view;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import db_project.db.dbGenerator.DBGenerator;
import db_project.db.tables.PathTable;
import db_project.model.Path;
import db_project.view.controller.SectionController;

public class TestSectionController {
    private static final DBGenerator dbGenerator = new DBGenerator();
    private SectionController sectionController;
    private PathTable pathTable;


    @Test
    public void testSectionController() {
        dbGenerator.createDB();
        dbGenerator.createTables();
        this.sectionController = new SectionController(dbGenerator);
        this.pathTable = (PathTable) dbGenerator.getTableByClass(PathTable.class);
        final Optional<Path> routePath = this.pathTable.findByPrimaryKey("BI-BZ");
        if (routePath.isEmpty()) {
            fail("primary key not found");
        }
        this.sectionController.computeSectionsFromPath(routePath.get());
        final var solution = this.sectionController.getPathDetails();
        assertFalse(solution.isEmpty());
        solution.forEach(System.out::println);
    }

}
