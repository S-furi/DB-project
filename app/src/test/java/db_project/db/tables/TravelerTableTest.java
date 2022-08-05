package db_project.db.tables;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import db_project.db.ConnectionProvider;
import db_project.model.Traveler;

public class TravelerTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);

  private static final TravelerTable travelerTable =
      new TravelerTable(connectionProvider.getMySQLConnection());

  // private final Date date = Utils.buildDate(25, 5, 2005).get();

  private final Traveler traveler =
      new Traveler("3", "Fabio", "DeLuigi", 34, "luigi@gmail.com", "Santa", Optional.of(0));

  @BeforeAll
  static void setUp() {
    final Traveler traveler1 =
        new Traveler("1", "Gianni", "Gianni", 57, "ciao@gmail.com", "Salerno", Optional.empty());
    final Traveler traveler2 =
        new Traveler(
            "2", "Mimmo", "Baresi", 24, "mimmombare@gmail.com", "Palermo", Optional.empty());

    assertTrue(travelerTable.save(traveler1));
    assertTrue(travelerTable.save(traveler2));
  }

  @AfterAll
  static void tearDown() {
    travelerTable.findAll().forEach(t -> travelerTable.delete(t.getTravelerCode()));
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(travelerTable.findByPrimaryKey("1").isPresent());
    assertFalse(travelerTable.findByPrimaryKey("2").isPresent());
  }

  @Test
  public void testSaveandDelete() {
    assertTrue(travelerTable.save(this.traveler));
    assertThrows(
        IllegalStateException.class,
        () -> {
          travelerTable.save(this.traveler);
        });
    assertTrue(travelerTable.delete(this.traveler.getTravelerCode()));
    assertFalse(travelerTable.delete(this.traveler.getTravelerCode()));
  }

  @Test
  public void testUpdate() {
    final var curTraveler = travelerTable.findByPrimaryKey("1");
    if (curTraveler.isEmpty()) {
      fail("Select Failed");
    }
    assertTrue(
        travelerTable.update(
            new Traveler(
                "1",
                this.traveler.getFirstName(),
                this.traveler.getLastName(),
                this.traveler.getPhone(),
                this.traveler.getEmail(),
                this.traveler.getResidence(),
                this.traveler.isGroup())));
    assertTrue(travelerTable.update(curTraveler.get()));
  }
}
