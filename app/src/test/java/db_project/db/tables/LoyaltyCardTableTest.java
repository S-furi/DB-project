package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.Test;

import db_project.db.ConnectionProvider;
import db_project.model.LoyaltyCard;

public class LoyaltyCardTableTest {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Ferrovia";

  private final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);

  private final LoyaltyCardTable loyaltyCardTable =
      new LoyaltyCardTable(this.connectionProvider.getMySQLConnection());

  private final LoyaltyCard loyaltyCard = new LoyaltyCard("3", 50, 3);

  @Test
  public void testFindAll() {
    assertFalse(this.loyaltyCardTable.findAll().isEmpty());
    assertTrue(this.loyaltyCardTable.findAll().size() == 2);
  }

  @Test
  public void testFindByPrimaryKey() {
    assertTrue(this.loyaltyCardTable.findByPrimaryKey("1").isPresent());
    assertFalse(this.loyaltyCardTable.findByPrimaryKey("8").isPresent());
  }

  @Test
  public void testSaveAndDelete() {
    assertTrue(this.loyaltyCardTable.save(this.loyaltyCard));
    assertThrows(
        IllegalStateException.class,
        () -> {
          this.loyaltyCardTable.save(this.loyaltyCard);
        });
    assertTrue(this.loyaltyCardTable.delete(this.loyaltyCard.getCardId()));
    assertFalse(this.loyaltyCardTable.delete(this.loyaltyCard.getCardId()));
  }

  @Test
  public void testUpdate() {
    final var currLoyaltyCard = this.loyaltyCardTable.findByPrimaryKey("1");
    if (currLoyaltyCard.isEmpty()) {
      fail("Select Failed");
    }
    assertTrue(
        this.loyaltyCardTable.update(
            new LoyaltyCard(
                "1", this.loyaltyCard.getPoints(), this.loyaltyCard.getDiscountPercentage())));
    assertTrue(this.loyaltyCardTable.update(currLoyaltyCard.get()));
  }
}
