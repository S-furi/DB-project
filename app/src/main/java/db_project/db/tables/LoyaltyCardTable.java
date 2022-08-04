package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import db_project.db.Table;
import db_project.db.queryUtils.ArrayQueryParser;
import db_project.db.queryUtils.QueryParser;
import db_project.db.queryUtils.QueryResult;
import db_project.model.LoyaltyCard;

public class LoyaltyCardTable implements Table<LoyaltyCard, String> {
  public static final String TABLE_NAME = "LOYALTY_CARD";

  private final Connection connection;
  private final QueryParser queryParser;

  public LoyaltyCardTable(final Connection connection) {
    this.connection = connection;
    this.queryParser = new ArrayQueryParser(this.connection);
  }

  @Override
  public String getTableName() {
    return TABLE_NAME;
  }

  @Override
  public Optional<LoyaltyCard> findByPrimaryKey(final String primaryKey) {
    final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codCarta = ?";
    final String[] params = {primaryKey};
    this.queryParser.computeSqlQuery(query, params);
    return this.getLoyaltyCardsFromQueryResult(this.queryParser.getQueryResult()).stream()
        .findAny();
  }

  @Override
  public List<LoyaltyCard> findAll() {
    final String query = "SELECT * FROM " + TABLE_NAME;
    this.queryParser.computeSqlQuery(query, null);
    return this.getLoyaltyCardsFromQueryResult(this.queryParser.getQueryResult());
  }

  @Override
  public boolean save(final LoyaltyCard loyaltyCard) {
    final String query =
        "INSERT INTO " + TABLE_NAME + "(codCarta, punti, percentualeSconto)" + "VALUES (?, ?, ?)";

    final Object[] params = {
      loyaltyCard.getCardId(), loyaltyCard.getPoints(), loyaltyCard.getDiscountPercentage()
    };

    return this.queryParser.computeSqlQuery(query, params);
  }

  @Override
  public boolean update(final LoyaltyCard loyaltyCard) {
    final String query =
        "UPDATE " + TABLE_NAME + " SET " + " punti = ?, percentualeSconto = ? WHERE codCarta = ?";
    final Object[] params = {
      loyaltyCard.getPoints(), loyaltyCard.getDiscountPercentage(), loyaltyCard.getCardId()
    };
    return this.queryParser.computeSqlQuery(query, params);
  }

  @Override
  public boolean delete(final String primaryKey) {
    final String query = "DELETE FROM " + TABLE_NAME + " WHERE codCarta = ?";
    final String[] params = {primaryKey};

    return this.queryParser.computeSqlQuery(query, params);
  }

  private List<LoyaltyCard> getLoyaltyCardsFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<LoyaltyCard> loyaltyCards = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              System.out.println(row.toString());
              final String cardId = (String) row.get("codCarta");
              final int points = (int) row.get("punti");
              final int discountPercentage = (int) row.get("percentualeSconto");
              loyaltyCards.add(new LoyaltyCard(cardId, points, discountPercentage));
            });
    return loyaltyCards;
  }
}
