package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import db_project.db.Table;
import db_project.db.queryUtils.ArrayQueryParser;
import db_project.db.queryUtils.QueryParser;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Passenger;

public class PassengerTable implements Table<Passenger, String> {

  public static final String TABLE_NAME = "PASSEGGERO";

  private final Connection connection;
  private final QueryParser queryParser;

  public PassengerTable(final Connection connection) {
    this.connection = connection;
    this.queryParser = new ArrayQueryParser(this.connection);
  }

  @Override
  public String getTableName() {
    return TABLE_NAME;
  }

  @Override
  public Optional<Passenger> findByPrimaryKey(String primaryKey) {
    final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codPasseggero = ?";
    final String[] params = {primaryKey};
    this.queryParser.computeSqlQuery(query, params);
    return this.getTravelersFromQueryResult(this.queryParser.getQueryResult()).stream().findAny();
  }

  @Override
  public List<Passenger> findAll() {
    final String query = "SELECT * FROM " + TABLE_NAME;
    this.queryParser.computeSqlQuery(query, null);
    return this.getTravelersFromQueryResult(this.queryParser.getQueryResult());
  }

  public int getHighestID() {
    final String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY codPasseggero DESC LIMIT 1";
    this.queryParser.computeSqlQuery(query, null);
    var passenger = this.getTravelersFromQueryResult(this.queryParser.getQueryResult());
    return Integer.parseInt(passenger.get(0).getTravelerCode());
  }

  @Override
  public boolean save(Passenger traveler) {
    final String query =
        "INSERT INTO "
            + TABLE_NAME
            + "(codPasseggero, nome, cognome, telefono, email, residenza, codComitiva)"
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    final Object[] params = {
      traveler.getTravelerCode(),
      traveler.getFirstName(),
      traveler.getLastName(),
      traveler.getPhone(),
      traveler.getEmail(),
      traveler.getResidence(),
      traveler.isGroup().isPresent() ? traveler.isGroup().get() : Optional.empty()
    };
    return this.queryParser.computeSqlQuery(query, params);
  }

  @Override
  public boolean update(Passenger updatedTraveler) {
    final String query =
        "UPDATE " + TABLE_NAME + " SET " + " codComitiva = ? WHERE codPasseggero = ?";
    final Object[] params = {
      updatedTraveler.isGroup().isPresent() ? updatedTraveler.isGroup().get() : Optional.empty(),
      updatedTraveler.getTravelerCode()};
    return this.queryParser.computeSqlQuery(query, params);
  }

  @Override
  public boolean delete(String primaryKey) {
    final String query = "DELETE FROM " + TABLE_NAME + " WHERE codPasseggero = ?";
    final Object[] params = {primaryKey};
    return this.queryParser.computeSqlQuery(query, params);
  }

  private List<Passenger> getTravelersFromQueryResult(final QueryResult result) {
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
              final Optional<Object> isGroup = Optional.ofNullable(row.get("codComitiva"));
              travelers.add(
                  new Passenger(
                      travelerCode, firstName, lastName, phone, email, residence, isGroup));
            });
    return travelers;
  }
}
