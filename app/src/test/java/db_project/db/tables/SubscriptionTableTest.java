package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Subscription;
import db_project.utils.Utils;

public class SubscriptionTableTest {
    private static final String username = "root";
    private static final String password = "123Test123";
    private static final String dbName = "Ferrovia";

    private static final ConnectionProvider connectionProvider = 
        new ConnectionProvider(username, password, dbName);

    private static final SubscriptionTable subscriptionTable =
        new SubscriptionTable(connectionProvider.getMySQLConnection());

    @BeforeAll
    public static void setUp() {
        PassengerTableTest.setUp();
        LoyaltyCardTableTest.setUp();
        final var sub1 = 
            new Subscription("1", "1", Utils.dateToSqlDate(Utils.buildDate(11, 12, 2001).get()));
        final var sub2 =    
        new Subscription("2", "2", Utils.dateToSqlDate(Utils.buildDate(11, 12, 2001).get()));

        assertTrue(subscriptionTable.save(sub1));
        assertTrue(subscriptionTable.save(sub2));
    }

    @AfterAll
    public static void tearDown() {
        subscriptionTable.findAll().forEach(t -> subscriptionTable.delete(t.getPassengerCode()));
        LoyaltyCardTableTest.tearDown();
        PassengerTableTest.tearDown();
    }

    @Test
    public void testFindAll() {
        assertFalse(subscriptionTable.findAll().isEmpty());
        assertTrue(subscriptionTable.findAll().size() == 2);
    }

    @Test
    public void testFindByPrimaryKey() {
        assertTrue(subscriptionTable.findByPrimaryKey("1").isPresent());
        assertTrue(subscriptionTable.findByPrimaryKey("2").isPresent());
        assertFalse(subscriptionTable.findByPrimaryKey("9").isPresent());
    }
}
