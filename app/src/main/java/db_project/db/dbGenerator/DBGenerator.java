package db_project.db.dbGenerator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import db_project.db.ConnectionProvider;
import db_project.db.JsonReadeable;
import db_project.db.Table;
import db_project.db.tables.AdminTable;
import db_project.db.tables.CarClassTable;
import db_project.db.tables.CarTable;
import db_project.db.tables.CityTable;
import db_project.db.tables.DriverTable;
import db_project.db.tables.GroupTable;
import db_project.db.tables.LoyaltyCardTable;
import db_project.db.tables.PassengerTable;
import db_project.db.tables.PathInfoTable;
import db_project.db.tables.PathTable;
import db_project.db.tables.RouteInfoTable;
import db_project.db.tables.SeatTable;
import db_project.db.tables.SectionTable;
import db_project.db.tables.StationManagerTable;
import db_project.db.tables.StationTable;
import db_project.db.tables.SubscriptionTable;
import db_project.db.tables.TicketDetailTable;
import db_project.db.tables.TicketTable;
import db_project.db.tables.TrainTable;

@SuppressWarnings("rawtypes")
public class DBGenerator {
  private final String USERNAME = "root";
  private final String PASSWORD = "123Test123";
  private final String DBNAME = "Railway";
  private final String URI = "jdbc:mysql://localhost:3306/";
  private final Logger logger = Logger.getLogger("DBGenerator");
  private List<Table> tables = new ArrayList<>();
  private ConnectionProvider connectionProvider;

  public DBGenerator() {
    logger.setLevel(Level.WARNING);
    this.connectionProvider = new ConnectionProvider(USERNAME, PASSWORD, DBNAME);
  }

  public boolean createDB() {
    final String query = "CREATE DATABASE " + DBNAME;
    try {
      final var connection = DriverManager.getConnection(URI, USERNAME, PASSWORD);
      final var resultSet = connection.getMetaData().getCatalogs();
      while (resultSet.next()) {
        final String catalog = resultSet.getString(1);
        if (catalog.equals(DBNAME)) {
          return false;
        }
      }

      final var statement = connection.createStatement();
      final boolean res = statement.executeUpdate(query) > 0;
      logger.info(
          res ? DBNAME + " Creation Succeed!" : DBNAME + " Creation Failed: DB already exists");
      return true;
    } catch (final SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  public boolean dropDB() {
    final String query = "DROP DATABASE " + DBNAME;
    try {
      final var connection = DriverManager.getConnection(URI, USERNAME, PASSWORD);
      final var resultSet = connection.getMetaData().getCatalogs();
      while (resultSet.next()) {
        final String catalog = resultSet.getString(1);
        if (catalog.equals(DBNAME)) {
          final var statement = connection.createStatement();
          final boolean res = statement.executeUpdate(query) > 0;
          logger.info(
              !res ? DBNAME + " Drop Succeed!" : DBNAME + " Drop Failed: DB doensn't exist");
          return true;
        }
      }
      return false;
    } catch (final SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  public boolean createTables() {
    final ConnectionProvider connectionProvider =
        new ConnectionProvider(USERNAME, PASSWORD, DBNAME);
    final var lst = this.getAllTables(connectionProvider.getMySQLConnection());
    this.tables = lst;
    if (this.areTablesAlreadyCreated()) {
      logger.info("*****TALBES ALREADY CREATED!***");
      this.tables.forEach(Table::setAlreadyCreated);
      return false;
    }
    this.tables.forEach(Table::createTable);
    this.constraintApplier(connectionProvider.getMySQLConnection());
    return true;
  }

  private boolean areTablesAlreadyCreated() {
    try (final Connection connection =
        DriverManager.getConnection(URI + DBNAME, USERNAME, PASSWORD)) {
      final var statement = connection.createStatement();
      final ResultSet rs = statement.executeQuery("Show Tables;");
      final List<String> lst = new ArrayList<String>();
      while (rs.next()) {
        lst.add(rs.getString(1));
      }
      return !lst.isEmpty() || !Collections.disjoint(this.tables, lst);
    } catch (final SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  private boolean constraintApplier(final Connection connection) {
    final List<String> queries =
        List.of(
            "alter table AMMINISTRATORE add constraint FKResidenza_Adm_FK "
                + "foreign key (residenza) "
                + "references CITTA (nome); ",
            "alter table BIGLIETTO add constraint FKAcquistoComitiva_FK "
                + "foreign key (codComitiva) "
                + "references COMITIVA (codComitiva); ",
            "alter table BIGLIETTO add constraint FKAcquisto_FK "
                + "foreign key (codPasseggero) "
                + "references PASSEGGERO (codPasseggero); ",
            "alter table BIGLIETTO add constraint FKRiferimento_FK "
                + "foreign key (codPercorso, codTreno, data) "
                + "references PERCORRENZA (codPercorso, codTreno, data); ",
            "alter table MACCHINISTA add constraint FKResidenza_Mac_FK "
                + "foreign key (residenza) "
                + "references CITTA (nome); ",
            "alter table PASSEGGERO add constraint FKResidenza_Pas_FK "
                + "foreign key (residenza) "
                + "references CITTA (nome); ",
            "alter table PASSEGGERO add constraint FKFormazione_FK "
                + "foreign key (codComitiva) "
                + "references COMITIVA (codComitiva); ",
            "alter table PERCORRENZA add constraint FKServizio_FK "
                + "foreign key (codTreno) "
                + "references TRENO (codTreno); ",
            "alter table PERCORRENZA add constraint FKAttivazione "
                + "foreign key (codPercorso) "
                + "references PERCORSO (codPercorso); ",
            "alter table PERCORSO add constraint FKAmministrazione_FK "
                + "foreign key (adminID) "
                + "references AMMINISTRATORE (adminID); ",
            "alter table RESPONSABILE_STAZIONE add constraint FKResidenza_Resp_FK "
                + "foreign key (residenza) "
                + "references CITTA (nome); ",
            "alter table STAZIONE add constraint FKLocazione_FK "
                + "foreign key (locazione) "
                + "references CITTA (nome); ",
            "alter table STAZIONE add constraint FKGestione_FK "
                + "foreign key (codResponsabile) "
                + "references RESPONSABILE_STAZIONE (codResponsabile); ",
            "alter table DETTAGLIO_PERCORSO add constraint REF_Strut_PERCO "
                + "foreign key (codPercorso) "
                + "references PERCORSO (codPercorso); ",
            "alter table DETTAGLIO_PERCORSO add constraint REF_Strut_TRATT_FK "
                + "foreign key (codTratta) "
                + "references TRATTA (codTratta); ",
            "alter table TRATTA add constraint FKPartenza_FK "
                + "foreign key (codStazionePartenza) "
                + "references STAZIONE (codStazione); ",
            "alter table TRATTA add constraint FKArrivo_FK "
                + "foreign key (codStazioneArrivo) "
                + "references STAZIONE (codStazione); ",
            "alter table TRENO add constraint FKPilota_FK "
                + "foreign key (codMacchinista) "
                + "references MACCHINISTA (numeroPatente); ",
            "alter table CARROZZA add constraint FKComposizione_FK "
                + "foreign key (codTreno) "
                + "references TRENO (codTreno); ",
            "alter table CARROZZA add constraint FKAppartenenza "
                + "foreign key (numClasse) "
                + "references CLASSE (numClasse); ",
            "alter table POSTO add constraint FKSuddivisione "
                + "foreign key (numClasse, codTreno, numeroCarrozza) "
                + "references CARROZZA (numClasse, codTreno, numeroCarrozza); ",
            "alter table SOTTOSCRIZIONE add constraint FKRiferimento_Pas_FK "
                + "foreign key (codPasseggero) "
                + "references PASSEGGERO (codPasseggero); ",
            "alter table SOTTOSCRIZIONE add constraint FKRiferimento_Card_FK "
                + "foreign key (codCarta) "
                + "references LOYALTY_CARD (codCarta); ",
            "alter table DETTAGLIO_BIGLIETTO add constraint FKRiseva_FK "
                + "foreign key (codiceBiglietto) "
                + "references BIGLIETTO (codiceBiglietto); ",
            "alter table DETTAGLIO_BIGLIETTO add constraint FKPer "
                + "foreign key (numClasse, codTreno, numeroCarrozza, numeroPosto)"
                + "references POSTO (numClasse, codTreno, numeroCarrozza, numeroPosto);");

    try (final var statement = connection.createStatement()) {
      for (final var query : queries) {
        statement.execute(query);
      }
      return true;
    } catch (final SQLException e) {
      throw new IllegalStateException(e);
    }
  }

  private List<Table> getAllTables(final Connection connection) {
    final Table adminTable = new AdminTable(connection);
    final Table carClassTable = new CarClassTable(connection);
    final Table carTable = new CarTable(connection);
    final Table cityTable = new CityTable(connection);
    final Table driverTable = new DriverTable(connection);
    final Table groupTable = new GroupTable(connection);
    final Table loyaltyCardTable = new LoyaltyCardTable(connection);
    final Table passengerTable = new PassengerTable(connection);
    final Table pathInfoTable = new PathInfoTable(connection);
    final Table pathTable = new PathTable(connection);
    final Table routeInfoTable = new RouteInfoTable(connection);
    final Table seatTable = new SeatTable(connection);
    final Table sectionTable = new SectionTable(connection);
    final Table stationManagerTable = new StationManagerTable(connection);
    final Table stationTable = new StationTable(connection);
    final Table subscritpionTable = new SubscriptionTable(connection);
    final Table ticketDetailTable = new TicketDetailTable(connection);
    final Table ticketTable = new TicketTable(connection);
    final Table trainTable = new TrainTable(connection);

    return List.of(
        adminTable,
        carClassTable,
        carTable,
        cityTable,
        driverTable,
        groupTable,
        loyaltyCardTable,
        passengerTable,
        pathInfoTable,
        pathTable,
        routeInfoTable,
        seatTable,
        sectionTable,
        stationManagerTable,
        stationTable,
        subscritpionTable,
        ticketDetailTable,
        ticketTable,
        trainTable);
  }

  public List<Table> getTables() {
    return this.tables;
  }

  public Table<?, ?> getTableByClass(Class<? extends Table<?, ?>> clazz) {
    final var elem = this.tables.stream().filter(clazz::isInstance).map(clazz::cast).findFirst();
    if (elem.isEmpty()) {
      throw new IllegalAccessError("Specified table does not exist!");
    }
    return elem.get();
  }

  public void populateTables() {
    final var connection = 
        new ConnectionProvider(USERNAME, PASSWORD, DBNAME).getMySQLConnection();
    final List<JsonReadeable> tbls = this.getJsonReadeables(connection);
    final Map<JsonReadeable, Boolean> createdTables = new HashMap<>();
    tbls.forEach(t -> createdTables.put(t, false));
    
    while (createdTables.values().contains(false)) {
      createdTables
        .entrySet()
        .stream()
        .filter(t -> t.getValue() == false)
        .map(t -> t.getKey())
        .forEach(tbl -> {
          if (tbl.saveToDb()) {
            createdTables.put(tbl, true);
          }
        });
    }
  }


  private List<JsonReadeable> getJsonReadeables(final Connection connection) {
    final JsonReadeable adminTable = new AdminTable(connection);
    final JsonReadeable carClassTable = new CarClassTable(connection);
    final JsonReadeable cityTable = new CityTable(connection);
    final JsonReadeable driverTable = new DriverTable(connection);
    final JsonReadeable groupTable = new GroupTable(connection);
    final JsonReadeable loyaltyCardTable = new LoyaltyCardTable(connection);
    final JsonReadeable passengerTable = new PassengerTable(connection);
    final JsonReadeable pathInfoTable = new PathInfoTable(connection);
    final JsonReadeable pathTable = new PathTable(connection);
    final JsonReadeable sectionTable = new SectionTable(connection);
    final JsonReadeable stationManagerTable = new StationManagerTable(connection);
    final JsonReadeable stationTable = new StationTable(connection);

    return List.of(
      adminTable,
      carClassTable,
      cityTable,
      driverTable,
      groupTable,
      loyaltyCardTable,
      passengerTable,
      pathInfoTable,
      pathTable,
      sectionTable,
      stationManagerTable,
      stationTable
    );
  }

  public ConnectionProvider getConnectionProvider() {
    return this.connectionProvider;
  }
}
