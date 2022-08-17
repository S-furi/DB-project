package db_project.queryParser;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import db_project.db.ConnectionProvider;
import db_project.db.queryUtils.ArrayQueryParser;
import db_project.db.queryUtils.QueryParser;
import db_project.db.queryUtils.TestParser;

class TestQueryParser {
  private static final String username = "root";
  private static final String password = "123Test123";
  private static final String dbName = "Railway";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, dbName);
  private final QueryParser parser = new ArrayQueryParser(connectionProvider.getMySQLConnection());

  @Test
  void testSelect() {
    assertTrue(TestParser.testSelect(TestParser.getParser()));
  }

  @Test
  void testInsert() {
    assertTrue(TestParser.testInsert(TestParser.getParser()));
  }

  @Test
  public void testStrangeQuery() {
    final String query = 
    "SELECT t.codTratta, Partenza.nome as StazionePartenza, Arrivo.nome as StazioneArrivo from   "
        + "( SELECT codTratta, nome from tratta, stazione where codStazioneArrivo = codStazione) as Arrivo, "
        + "( SELECT codTratta, nome from tratta, stazione where codStazionePartenza = codStazione) as Partenza, "
        + "tratta t "
        + "where t.codTratta = Arrivo.codTratta "
        + "and t.codTratta = Partenza.codTratta "
        + "GROUP BY t.codTratta, StazionePartenza, StazioneArrivo ";
    this.parser.computeSqlQuery(query, null);
    System.out.println(this.parser.getQueryResult().toString()); 
  }

}
