package db_project.view;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import db_project.db.dbGenerator.DBGenerator;
import db_project.view.adminPanelController.PathController;

public class TestRailwayController {
  private static final DBGenerator dbGenerator = new DBGenerator();
  private PathController railwayController;

  @AfterAll
  public static void tearDown() {
    dbGenerator.dropDB();
  }

  @Test
  public void testTripSolution() {
    dbGenerator.createDB();
    dbGenerator.createTables();
    dbGenerator.populateTables();

    this.railwayController = new PathController(dbGenerator);

    final String srcStation = "BOLOGNA C.LE";
    final String dstStation = "BOLZANO";
    final var solution = this.railwayController.getTripSolution(srcStation, dstStation);
    assertTrue(solution.isPresent());
    System.out.println(solution.get());
    final var solutions = this.railwayController.getAllTripSolutions();
    assertFalse(solutions.isEmpty());
    solutions.stream().map(t -> t.get()).forEach(System.out::println);
    System.out.println(solutions.size());
  }
}
