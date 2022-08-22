package db_project.buildDb;

import org.junit.jupiter.api.Test;

import db_project.db.dbGenerator.DBGenerator;

public class BuildDb {
  private final DBGenerator dbGenerator = new DBGenerator();

  @Test
  public void generateDB() {
    if (!dbGenerator.createDB()) {
      System.out.println(
          "An error occurred during building DB.\nTry to check if DB is already created.");
      System.exit(1);
    }
    System.out.println("Generating database...");
    dbGenerator.createTables();
    System.out.println("Filling Tables...");
    dbGenerator.populateTables();
  }

  @Test
  public void dropDB() {
    if (dbGenerator.dropDB()) {
      System.out.println("Database succesfully dropped!");
    } else {
      System.out.println("An error occurred during dropping.\nTry to check if DB exits and retry.");
    }
  }
}
