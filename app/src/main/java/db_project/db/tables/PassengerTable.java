package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Passenger;

public class PassengerTable extends AbstractTable<Passenger, String> {
  public static final String TABLE_NAME = "PASSEGGERO";
  public static final String PRIMARY_KEY = "codPasseggero";

  public PassengerTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(
        List.of("nome", "cognome", "telefono", "email", "residenza", "codComitiva"));
  }

  @Override
  protected Object[] getSaveQueryParameters(final Passenger passenger) {
    return new Object[] {
      passenger.getTravelerCode(),
      passenger.getFirstName(),
      passenger.getLastName(),
      passenger.getPhone(),
      passenger.getEmail(),
      passenger.getResidence(),
      passenger.isGroup()
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
      passenger.isGroup(),
      passenger.getTravelerCode()
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
              System.out.println(row.toString());
              final String travelerCode = (String) row.get("codPasseggero");
              final String firstName = (String) row.get("nome");
              final String lastName = (String) row.get("cognome");
              final String phone = (String) row.get("telefono");
              final String email = (String) row.get("email");
              final String residence = (String) row.get("residenza");
              final Optional<String> isGroup = Optional.ofNullable((String) row.get("codComitiva"));
              travelers.add(
                  new Passenger(
                      travelerCode, firstName, lastName, phone, email, residence, isGroup));
            });
    return travelers;
  }

  public int getHighestID() {
    final String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY codPasseggero DESC LIMIT 1";
    super.parser.computeSqlQuery(query, null);
    var passenger = this.getPrettyResultFromQueryResult(super.parser.getQueryResult());
    return Integer.parseInt(passenger.get(0).getTravelerCode());
  }
}
