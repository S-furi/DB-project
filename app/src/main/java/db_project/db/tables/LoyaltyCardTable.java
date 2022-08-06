package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.LoyaltyCard;

public class LoyaltyCardTable extends AbstractTable<LoyaltyCard, String> {
  public static final String TABLE_NAME = "LOYALTY_CARD";
  public static final String PRIMARY_KEY = "codCarta";

  public LoyaltyCardTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of(
        "punti",
        "percentualeSconto"));
  }

  @Override
  protected Object[] getSaveQueryParameters(final LoyaltyCard loyaltyCard) {
    return new Object[] {
        loyaltyCard.getCardId(),
        loyaltyCard.getPoints(),
        loyaltyCard.getDiscountPercentage()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final LoyaltyCard loyaltyCard) {
    return new Object[] {
        loyaltyCard.getPoints(),
        loyaltyCard.getDiscountPercentage(),
        loyaltyCard.getCardId()
    };
  }

  @Override
  protected List<LoyaltyCard> getPrettyResultFromQueryResult(QueryResult result) {
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
