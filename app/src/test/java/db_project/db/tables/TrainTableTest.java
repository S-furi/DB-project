package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Train;

public class TrainTableTest {

  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);
  private static final TrainTable trainTable =
      new TrainTable(connectionProvider.getMySQLConnection());

  final Train train3 = new Train("2", "1", 100, "1");

  @BeforeAll
  static void setUp() {
    DriverTableTest.setUp();
    final Train train1 = new Train("1", "2", 200, "0");

    assertTrue(trainTable.save(train1));
  }

  @AfterAll
  static void tearDown() {
    trainTable.findAll().forEach(t -> trainTable.delete(t.getTrainCode()));
    DriverTableTest.tearDown();
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(trainTable.findByPrimaryKey("1").isPresent());
    assertFalse(trainTable.findByPrimaryKey("9").isPresent());
  }

  @Test
  public void findAll() {
    assertFalse(trainTable.findAll().isEmpty());
    assertTrue(trainTable.findAll().size() == 1);
  }

  @Test
  public void testSaveandDelete() {
    assertTrue(trainTable.save(this.train3));
    assertThrows(
        IllegalStateException.class,
        () -> {
          trainTable.save(this.train3);
        });
    assertTrue(trainTable.delete(this.train3.getTrainCode()));
    assertFalse(trainTable.delete(this.train3.getTrainCode()));
  }

  @Test
  public void testUpdate() {
    final var currTrain = trainTable.findByPrimaryKey("1");
    if (currTrain.isEmpty()) {
      fail("Select Failed");
    }
    assertTrue(
        trainTable.update(
            new Train(
                "1",
                this.train3.getLicenseNumber(),
                this.train3.getCapacity(),
                this.train3.isRegionaleVeloce())));
    assertTrue(trainTable.update(currTrain.get()));
  }
}
