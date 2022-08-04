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
import db_project.model.Admin;
import db_project.utils.Utils;

public class AdminTable implements Table<Admin, String> {
  public static final String TABLE_NAME = "AMMINISTRATORE";
  private final Connection connection;
  private final QueryParser queryParser;

  public AdminTable(final Connection connection) {
    this.connection = connection;
    this.queryParser = new ArrayQueryParser(this.connection);
  }

  @Override
  public String getTableName() {
    return TABLE_NAME;
  }

  @Override
  public Optional<Admin> findByPrimaryKey(final String primaryKey) {
    final String query = "SELECT * FROM " + TABLE_NAME + " WHERE adminID = ?";
    final String[] params = {primaryKey};
    this.queryParser.computeSqlQuery(query, params);
    return this.readAdminsWithQueryResult(this.queryParser.getQueryResult()).stream().findFirst();
  }

  @Override
  public List<Admin> findAll() {
    final String query = "SELECT * FROM " + TABLE_NAME;
    this.queryParser.computeSqlQuery(query, null);
    return this.readAdminsWithQueryResult(this.queryParser.getQueryResult());
  }

  @Override
  public boolean save(final Admin admin) {
    final String query =
        "INSERT INTO "
            + TABLE_NAME
            + " (adminID, annoContratto, nome, cognome, telefono, email, residenza)"
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    final Object[] params = {
      admin.getId(),
      Utils.dateToSqlDate(admin.getContractYear()),
      admin.getFirstName(),
      admin.getLastName(),
      admin.getTelephone(),
      admin.getEmail(),
      admin.getResidence()
    };

    return this.queryParser.computeSqlQuery(query, params);
  }

  @Override
  public boolean delete(final String primaryKey) {
    String query = "DELETE FROM " + TABLE_NAME + " WHERE adminID = ?";
    String[] params = {primaryKey};
    return this.queryParser.computeSqlQuery(query, params);
  }

  @Override
  public boolean update(final Admin admin) {
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
            + " WHERE adminID = ?";

    final Object[] params = {
      Utils.dateToSqlDate(admin.getContractYear()),
      admin.getFirstName(),
      admin.getLastName(),
      admin.getTelephone(),
      admin.getEmail(),
      admin.getResidence(),
      admin.getId()
    };

    return this.queryParser.computeSqlQuery(query, params);
  }

  private List<Admin> readAdminsWithQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    final List<Admin> admList = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              System.out.println(row.toString());
              final String id = (String) row.get("adminID");
              final Date contractYear = (Date) row.get("annoContratto");
              final String firstName = (String) row.get("nome");
              final String lastName = (String) row.get("cognome");
              final int telephone = (int) row.get("telefono");
              final String email = (String) row.get("email");
              final String residence = (String) row.get("residenza");
              admList.add(
                  new Admin(id, contractYear, firstName, lastName, telephone, email, residence));
            });
    return admList;
  }
}
