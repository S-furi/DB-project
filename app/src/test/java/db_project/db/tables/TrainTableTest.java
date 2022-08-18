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

  private final Train train = new Train("2", "2", 200, true);

  @BeforeAll
  public static void setUp() {
    DriverTableTest.setUp();
    final var train1 = new Train("1", "1", 300, false);
    trainTable.save(train1);
  }

  @AfterAll
  public static void tearDown() {
    trainTable.findAll().forEach(t -> trainTable.delete(t.getTrainCode()));
    DriverTableTest.tearDown();
  }

  @Test
  public void testFindAll() {
    assertFalse(trainTable.findAll().isEmpty());
    assertTrue(trainTable.findAll().size() == 1);
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(trainTable.findByPrimaryKey("1").isPresent());
    assertTrue(trainTable.findByPrimaryKey("1").get().getLicenseNumber().equals("1"));
    assertFalse(trainTable.findByPrimaryKey("9").isPresent());
  }

  @Test
  public void testSaveAndDelete() {
    assertTrue(trainTable.save(this.train));
    assertFalse(trainTable.save(this.train));
    assertTrue(trainTable.delete(this.train.getTrainCode()));
    assertFalse(trainTable.delete(this.train.getTrainCode()));
  }

  @Test
  public void findAll() {
    assertFalse(trainTable.findAll().isEmpty());
    assertTrue(trainTable.findAll().size() == 1);
  }

  @Test
  public void testUpdate() {
    final var currTrain = trainTable.findByPrimaryKey("1");
    if (currTrain.isEmpty()) {
      fail("Select Failed");
    }
    final var newTrain =
        new Train("1", this.train.getLicenseNumber(), this.train.getCapacity(), this.train.getIsRv());

    assertTrue(trainTable.update(newTrain));
    assertTrue(trainTable.update(currTrain.get()));
  }
}
