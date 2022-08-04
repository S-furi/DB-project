package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import db_project.db.Table;
import db_project.db.queryUtils.ArrayQueryParser;
import db_project.db.queryUtils.QueryParser;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Driver;
import db_project.utils.Utils;

public class DriverTable implements Table<Driver, String> {
  public static final String TABLE_NAME = "MACCHINISTA";
  private final Connection connection;
  private final QueryParser queryParser;

  public DriverTable(final Connection connection) {
    this.connection = connection;
    this.queryParser = new ArrayQueryParser(this.connection);
  }

  @Override
  public String getTableName() {
    return TABLE_NAME;
  }

  @Override
  public Optional<Driver> findByPrimaryKey(final String primaryKey) {
    final String query = "SELECT * FROM " + TABLE_NAME + " WHERE numeroPatente = ?";
    final String[] params = {primaryKey};
    this.queryParser.computeSqlQuery(query, params);
    return this.readDriverFromQueryResult(this.queryParser.getQueryResult()).stream().findAny();
  }

  @Override
  public List<Driver> findAll() {
    final String query = "SELECT * FROM " + TABLE_NAME;
    this.queryParser.computeSqlQuery(query, null);
    return this.readDriverFromQueryResult(this.queryParser.getQueryResult());
  }

  @Override
  public boolean save(final Driver driver) {
    final String query =
        "INSERT INTO "
            + TABLE_NAME
            + " ("
            + "numeroPatente, annoContratto, nome, cognome,"
            + "telefono, email, residenza)"
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    final Object[] params = {
      driver.getLicenceNumber(),
      Utils.dateToSqlDate(driver.getContractYear()),
      driver.getFirstName(),
      driver.getLastName(),
      driver.getTelephone(),
      driver.getEmail(),
      driver.getResidence()
    };

    return this.queryParser.computeSqlQuery(query, params);
  }

  @Override
  public boolean update(final Driver driver) {
    final String query =
        "UPDATE "
            + TABLE_NAME
            + " SET "
            + "annoContratto = ?,"
            + "nome = ?,"
            + "cognome = ?,"
            + "telefono = ?,"
            + "email = ?,"
            + "residenza = ?"
            + " WHERE numeroPatente = ?";

    final Object[] params = {
      Utils.dateToSqlDate(driver.getContractYear()),
      driver.getFirstName(),
      driver.getLastName(),
      driver.getTelephone(),
      driver.getEmail(),
      driver.getResidence(),
      driver.getLicenceNumber()
    };

    return this.queryParser.computeSqlQuery(query, params);
  }

  @Override
  public boolean delete(final String primaryKey) {
    String query = "DELETE FROM " + TABLE_NAME + " WHERE numeroPatente = ?";
    String[] params = {primaryKey};
    return this.queryParser.computeSqlQuery(query, params);
  }

  private List<Driver> readDriverFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    final List<Driver> drivers = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              System.out.println(row.toString());
              final String licenceNumber = (String) row.get("numeroPatente");
              final Date contractYear = (Date) row.get("annoContratto");
              final String firstName = (String) row.get("nome");
              final String lastName = (String) row.get("cognome");
              final int telephone = (int) row.get("telefono");
              final String email = (String) row.get("email");
              final String residence = (String) row.get("residenza");
              drivers.add(
                  new Driver(
                      licenceNumber,
                      contractYear,
                      firstName,
                      lastName,
                      telephone,
                      email,
                      residence));
            });
    return drivers;
  }
}
