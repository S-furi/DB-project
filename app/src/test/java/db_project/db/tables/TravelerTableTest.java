package db_project.db.tables;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.Test;

import db_project.db.ConnectionProvider;
import db_project.model.Traveler;
import db_project.utils.Utils;

public class TravelerTableTest {
    private static final String username = "root";
    private static final String password = "123Test123";
    private static final String dbName = "Ferrovia";
    
    private final ConnectionProvider connectionProvider = new ConnectionProvider(username, password, dbName);
    
    private final TravelerTable travelerTable = new TravelerTable(connectionProvider.getMySQLConnection());

    private final Date date = Utils.buildDate(25, 5, 2005).get();
    private final Traveler traveler = 
        new Traveler("1", "Gianni", "Gianni", 57,"ciao@gmail.com", "Salerno", "false");
    
    @Test
    public void testFindByPrimaryKey(){
        assertTrue(this.travelerTable.findByPrimaryKey("1").isPresent());
        assertFalse(this.travelerTable.findByPrimaryKey("2").isPresent());
    }
}
