package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import db_project.db.AbstractTable;
import db_project.db.JsonReadeable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Passenger;
import db_project.utils.AbstractJsonReader;

public class PassengerTable extends AbstractTable<Passenger, String>
    implements JsonReadeable<Passenger> {
  public static final String TABLE_NAME = "PASSEGGERO";
  public static final String PRIMARY_KEY = "codPasseggero";
  private final Logger logger;

  public PassengerTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(
        List.of("nome", "cognome", "telefono", "email", "residenza"));

    this.logger = Logger.getLogger("CityTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(final Passenger passenger) {
    return new Object[] {
      passenger.getPassengerCode(),
      passenger.getFirstName(),
      passenger.getLastName(),
      passenger.getPhone(),
      passenger.getEmail(),
      passenger.getResidence()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Passenger passenger) {
    return new Object[] {
      passenger.getFirstName(),
      passenger.getLastName(),
      passenger.getPhone(),
      passenger.getEmail(),
      passenger.getResidence(),
      passenger.getPassengerCode()
    };
  }

  @Override
  protected List<Passenger> getPrettyResultFromQueryResult(QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<Passenger> travelers = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              logger.info(row.toString());
              final String travelerCode = (String) row.get("codPasseggero");
              final String firstName = (String) row.get("nome");
              final String lastName = (String) row.get("cognome");
              final String phone = (String) row.get("telefono");
              final String email = (String) row.get("email");
              final String residence = (String) row.get("residenza");
              travelers.add(
                  new Passenger(
                      travelerCode, firstName, lastName, phone, email, residence));
            });
    return travelers;
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table PASSEGGERO ( "
            + "codPasseggero varchar(5) not null, "
            + "nome varchar(40) not null, "
            + "cognome varchar(40) not null, "
            + "telefono varchar(10) not null, "
            + "email varchar(50) not null, "
            + "residenza varchar(40) not null, "
            + "constraint ID_PASSEGGERO_ID primary key (codPasseggero));";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  public int getHighestID() {
    final String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY codPasseggero DESC LIMIT 1";
    super.parser.computeSqlQuery(query, null);
    var passenger = this.getPrettyResultFromQueryResult(super.parser.getQueryResult());
    return passenger.isEmpty() ? 0 : Integer.parseInt(passenger.get(0).getPassengerCode());
  }

  @Override
  public List<Passenger> readFromFile() {
    return new AbstractJsonReader<Passenger>() {}.setFileName("DbPassengers.json")
        .retreiveData(Passenger.class);
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
