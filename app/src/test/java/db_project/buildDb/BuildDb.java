package db_project.buildDb;

import org.junit.jupiter.api.Test;

import db_project.db.dbGenerator.DBGenerator;

public class BuildDb {
  private final DBGenerator dbGenerator = new DBGenerator();

  @Test
  public void generateDB() {
    if (!dbGenerator.createDB()) {
      System.out.println("Database already created!");
      System.exit(1);
    }
    System.out.println("Generating database...");
    dbGenerator.createTables();
    dbGenerator.populateTables();
  }

  @Test
  public void dropDB() {
    dbGenerator.dropDB();
  }
}
