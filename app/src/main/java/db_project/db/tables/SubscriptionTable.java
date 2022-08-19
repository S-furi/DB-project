package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import db_project.db.AbstractTable;
import db_project.db.JsonReadeable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Subscription;
import db_project.utils.AbstractJsonReader;

public class SubscriptionTable extends AbstractTable<Subscription, String>
    implements JsonReadeable<Subscription> {
  public static final String TABLE_NAME = "SOTTOSCRIZIONE";
  public static final String PRIMARY_KEY = "codPasseggero";
  private final Logger logger;

  public SubscriptionTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("codCarta", "dataSottoscrizione"));
    this.logger = Logger.getLogger("SubscriptionTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(final Subscription subscription) {
    return new Object[] {
      subscription.getPassengerCode(),
      subscription.getCardNumber(),
      subscription.getSubscriptionDate()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Subscription subscription) {
    return new Object[] {
      subscription.getCardNumber(),
      subscription.getSubscriptionDate(),
      subscription.getPassengerCode()
    };
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table SOTTOSCRIZIONE ( "
            + "codPasseggero varchar(5) not null, "
            + "codCarta varchar(5) not null, "
            + "dataSottoscrizione date not null, "
            + "constraint FKRiferimento_Pas_ID primary key (codPasseggero), "
            + "constraint FKRiferimento_Card_ID unique (codCarta)); ";

    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected List<Subscription> getPrettyResultFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<Subscription> subscriptions = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              this.logger.info(row.toString());
              final String passengerCode = (String) row.get("codPasseggero");
              final String cardCode = (String) row.get("codCarta");
              final Date subscriptionDate = (Date) row.get("dataSottoscrizione");
              subscriptions.add(new Subscription(passengerCode, cardCode, subscriptionDate));
            });
    return subscriptions;
  }

  @Override
  public List<Subscription> readFromFile() {
    return new AbstractJsonReader<Subscription>() {}.setFileName("DbSubscriptions.json")
        .retreiveData(Subscription.class);
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
}
