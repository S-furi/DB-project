package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import db_project.db.AbstractTable;
import db_project.db.JsonReadeable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.LoyaltyCard;
import db_project.utils.AbstractJsonReader;

public class LoyaltyCardTable extends AbstractTable<LoyaltyCard, String>
    implements JsonReadeable<LoyaltyCard> {
  public static final String TABLE_NAME = "LOYALTY_CARD";
  public static final String PRIMARY_KEY = "codCarta";
  private final Logger logger;

  public LoyaltyCardTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("punti", "percentualeSconto"));
    this.logger = Logger.getLogger("CityTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(final LoyaltyCard loyaltyCard) {
    return new Object[] {
      loyaltyCard.getCardId(), loyaltyCard.getPoints(), loyaltyCard.getDiscountPercentage()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final LoyaltyCard loyaltyCard) {
    return new Object[] {
      loyaltyCard.getPoints(), loyaltyCard.getDiscountPercentage(), loyaltyCard.getCardId()
    };
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table LOYALTY_CARD ( "
            + "codCarta varchar(5) not null, "
            + "punti int not null, "
            + "percentualeSconto int not null, "
            + "constraint ID_LOYALTY_CARD_ID primary key (codCarta));";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
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
              logger.info(row.toString());
              final String cardId = (String) row.get("codCarta");
              final int points = (int) row.get("punti");
              final int discountPercentage = (int) row.get("percentualeSconto");
              loyaltyCards.add(new LoyaltyCard(cardId, points, discountPercentage));
            });
    return loyaltyCards;
  }

  @Override
  public List<LoyaltyCard> readFromFile() {
    return new AbstractJsonReader<LoyaltyCard>() {}.setFileName("DbLoyaltyCards.json")
        .retreiveData(LoyaltyCard.class);
  }

  @Override
  public boolean saveToDb() {
    for (final var elem : this.readFromFile()) {
      try {
        if (!this.save(elem)) {
          return false;
        }
      } catch (final IllegalStateException e) {
        return false;
      }
    }
    return true;
  }

  public boolean updateUserLoyaltyCardPoints(final String userId, final int distance) {
    final String query =
        "SELECT l.codCarta, punti, percentualeSconto "
            + "from LOYALTY_CARD l, SOTTOSCRIZIONE s, PASSEGGERO p "
            + "where p.codPasseggero = s.codPasseggero "
            + "and p.codPasseggero = ? "
            + "and s.codCarta = l.codCarta; ";
    final Object[] params = {userId};

    super.parser.computeSqlQuery(query, params);
    final var card =
        this.getPrettyResultFromQueryResult(super.parser.getQueryResult()).stream().findFirst();

    if (card.isEmpty()) {
      return false;
    }
    final int points = card.get().getPoints() + (distance / 5);

    return super.update(
        new LoyaltyCard(card.get().getCardId(), points, (int) Math.ceil(points / 30)));
  }
}
