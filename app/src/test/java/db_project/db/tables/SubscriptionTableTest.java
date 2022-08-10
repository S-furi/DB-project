package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Subscription;

public class SubscriptionTableTest {
    
    private static final String username = "root";
    private static final String password = "123Test123";
    private static final String dbName = "Ferrovia";

    private static final ConnectionProvider connectionProvider = 
        new ConnectionProvider(username, password, dbName);

    private static final SubscriptionTable subscriptionTable = 
        new SubscriptionTable(connectionProvider.getMySQLConnection());

    private final Subscription sub = new Subscription("2", "2", new Date(13022002));

    @BeforeAll
    static void setUp(){
        LoyaltyCardTableTest.setUp();
        PassengerTableTest.setUp();

        final Subscription subscription1 = new Subscription("1", "1", new Date(12012001));
        
        assertTrue(subscriptionTable.save(subscription1));
    }

    @AfterAll
    static void tearDown(){
        subscriptionTable.findAll().forEach(t -> subscriptionTable.delete(t.getTravelerCode()));
    }

    @Test
    public void testFindByPrimaryKey(){
        assertTrue(subscriptionTable.findByPrimaryKey("1").isPresent());
        assertFalse(subscriptionTable.findByPrimaryKey("8").isPresent());
    }

    @Test
    public void testFindAll(){
        assertFalse(subscriptionTable.findAll().isEmpty());
        assertTrue(subscriptionTable.findAll().size() == 1);
    }

    @Test
    public void testSaveAndDelete(){
        assertTrue(subscriptionTable.save(this.sub));
        assertFalse(subscriptionTable.save(this.sub));
        assertTrue(subscriptionTable.delete(this.sub.getTravelerCode()));
        assertFalse(subscriptionTable.delete(this.sub.getTravelerCode()));
    }

    @Test
    public void testUpdate(){
        final var currSubscription = subscriptionTable.findByPrimaryKey("1");
        if (currSubscription.isEmpty()){
            fail("Select Failed");
        }
        assertTrue(subscriptionTable.update(new Subscription("1", this.sub.getCardNumber(), this.sub.getSubscriptionDate())));
        assertTrue(subscriptionTable.update(currSubscription.get()));
    }
}
