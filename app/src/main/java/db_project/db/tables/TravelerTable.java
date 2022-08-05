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
import db_project.model.Traveler;

public class TravelerTable implements Table<Traveler, String> {

  public static final String TABLE_NAME = "PASSEGGERO";

  private final Connection connection;
  private final QueryParser queryParser;

  public TravelerTable(final Connection connection) {
    this.connection = connection;
    this.queryParser = new ArrayQueryParser(this.connection);
  }

  @Override
  public String getTableName() {
    return TABLE_NAME;
  }

  @Override
  public Optional<Traveler> findByPrimaryKey(String primaryKey) {
    final String query = "SELECT * FROM " + TABLE_NAME + " WHERE codPasseggero = ?";
    final String[] params = {primaryKey};
    this.queryParser.computeSqlQuery(query, params);
    return this.getTravelersFromQueryResult(this.queryParser.getQueryResult()).stream().findAny();
  }

  @Override
  public List<Traveler> findAll() {
    final String query = "SELECT * FROM " + TABLE_NAME;
    this.queryParser.computeSqlQuery(query, null);
    return this.getTravelersFromQueryResult(this.queryParser.getQueryResult());
  }

  @Override
  public boolean save(Traveler traveler) {
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
      traveler.isGroup().get()
    };
    return this.queryParser.computeSqlQuery(query, params);
  }

  @Override
  public boolean update(Traveler updatedTraveler) {
    final String query =
        "UPDATE " + TABLE_NAME + " SET " + " codComitiva = ? WHERE codPasseggero = ?";
    final Object[] params = {updatedTraveler.isGroup().get(), updatedTraveler.getTravelerCode()};
    return this.queryParser.computeSqlQuery(query, params);
  }

  @Override
  public boolean delete(String primaryKey) {
    final String query = "DELETE FROM " + TABLE_NAME + " WHERE codPasseggero = ?";
    final Object[] params = {primaryKey};
    return this.queryParser.computeSqlQuery(query, params);
  }

  private List<Traveler> getTravelersFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<Traveler> travelers = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              System.out.println(row.toString());
              final String travelerCode = (String) row.get("codPasseggero");
              final String firstName = (String) row.get("nome");
              final String lastName = (String) row.get("cognome");
              final int phone = (int) row.get("telefono");
              final String email = (String) row.get("email");
              final String residence = (String) row.get("residenza");
              final Optional<Object> isGroup = Optional.of(row.get("codComitiva"));
              travelers.add(
                  new Traveler(
                      travelerCode, firstName, lastName, phone, email, residence, isGroup));
            });
    return travelers;
  }
}
