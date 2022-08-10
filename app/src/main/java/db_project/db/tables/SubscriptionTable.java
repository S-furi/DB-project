package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Subscription;

public class SubscriptionTable extends AbstractTable<Subscription, String>{

    public static final String TABLE_NAME = "SOTTOSCRIZIONE";
    public static final String PRIMARY_KEY = "codPasseggero";

    public SubscriptionTable(final Connection connection){
        super(TABLE_NAME, connection);
        super.setPrimaryKey(PRIMARY_KEY);
        super.setTableColumns(List.of("codCarta", "dataSottoscrizione"));
    }

    @Override
    protected Object[] getSaveQueryParameters(Subscription subscription) {
        return new Object[]{subscription.getTravelerCode(), subscription.getCardNumber(), subscription.getSubscriptionDate()};
    }

    @Override
    protected Object[] getUpdateQueryParameters(Subscription subscription) {
        return new Object[]{subscription.getCardNumber(), subscription.getSubscriptionDate(), subscription.getTravelerCode()};
    }

    @Override
    protected List<Subscription> getPrettyResultFromQueryResult(QueryResult result) {
        if(result.getResult().isEmpty()){
            return Collections.emptyList();
        }
        List<Subscription> subscriptions = new ArrayList<>();
        result.getResult().get().forEach(row -> {
            System.out.println(row.toString());
            final String travelerCode = (String) row.get("codPasseggero");
            final String cardNumber = (String) row.get("codCarta");
            final Date subscriptionDate = (Date) row.get("dataSottoscrizione");
            subscriptions.add(new Subscription(travelerCode, cardNumber, subscriptionDate));
        });
        return subscriptions;
    }
    
    
}
