package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import db_project.db.ConnectionProvider;
import db_project.model.LoyaltyCard;

public class LoyaltyCardTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);

  private static final LoyaltyCardTable loyaltyCardTable =
      new LoyaltyCardTable(connectionProvider.getMySQLConnection());

  private final LoyaltyCard loyaltyCard = new LoyaltyCard("3", 50, 3);

  @BeforeAll
  static void setUp() {
    final var loyaltyCard1 = new LoyaltyCard("1", 100, 12);
    final var loyaltyCard2 = new LoyaltyCard("2", 50, 6);

    assertTrue(loyaltyCardTable.save(loyaltyCard1));
    assertTrue(loyaltyCardTable.save(loyaltyCard2));
    System.out.println("LAMMERDA!!!");
  }

  @AfterAll
  static void tearDown() {
    loyaltyCardTable.findAll().forEach(t -> loyaltyCardTable.delete(t.getCardId()));
  }

  @Test
  public void testFindAll() {
    assertFalse(loyaltyCardTable.findAll().isEmpty());
    assertTrue(loyaltyCardTable.findAll().size() == 2);
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(loyaltyCardTable.findByPrimaryKey("1").isPresent());
    assertFalse(loyaltyCardTable.findByPrimaryKey("8").isPresent());
  }

  @Test
  public void testSaveAndDelete() {
    assertTrue(loyaltyCardTable.save(this.loyaltyCard));
    assertThrows(
        IllegalStateException.class,
        () -> {
          loyaltyCardTable.save(this.loyaltyCard);
        });
    assertTrue(loyaltyCardTable.delete(this.loyaltyCard.getCardId()));
    assertFalse(loyaltyCardTable.delete(this.loyaltyCard.getCardId()));
  }

  @Test
  public void testUpdate() {
    final var currLoyaltyCard = loyaltyCardTable.findByPrimaryKey("1");
    if (currLoyaltyCard.isEmpty()) {
      fail("Select Failed");
    }
    assertTrue(
        loyaltyCardTable.update(
            new LoyaltyCard(
                "1", this.loyaltyCard.getPoints(), this.loyaltyCard.getDiscountPercentage())));
    assertTrue(loyaltyCardTable.update(currLoyaltyCard.get()));
  }
}
