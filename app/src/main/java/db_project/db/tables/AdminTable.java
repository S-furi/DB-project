package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import db_project.db.Table;
import db_project.db.queryUtils.ArrayQueryParser;
import db_project.db.queryUtils.QueryParser;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Admin;
import db_project.utils.Utils;
import javafx.util.Pair;

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
    String query = "SELECT * FROM " + TABLE_NAME + " WHERE adminID = ?";
    String[] params = {primaryKey};
    this.queryParser.computeSqlQuery(query, params);
    return this.readAdminsWithQueryResult(this.queryParser.getQueryResult()).stream().findFirst();
  }

  @Override
  public List<Admin> findAll() {
    String query = "SELECT * FROM " + TABLE_NAME;
    this.queryParser.computeSqlQuery(query, null);
    return this.readAdminsWithQueryResult(this.queryParser.getQueryResult());
  }

  @Override
  public boolean save(final Admin admin) {
    String query =
        "INSERT INTO "
            + TABLE_NAME
            + " (adminID, annoContratto, nome, cognome, telefono, email, residenza)"
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    Object[] params = {
      admin.getId(),
      Utils.dateToSqlDate(admin.getContractYear()),
      admin.getFirstName(),
      admin.getLastName(),
      admin.getTelephone(),
      admin.getEmail(),
      admin.getResidence()
    };

    if (this.queryParser.computeSqlQuery(query, params)) {
      return true;
    }
    return false;
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

    Object[] params = {
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
            res -> {
              res.forEach(System.out::println);
              final String id =
                  (String) this.getResultColumnValue(res, t -> t.getKey().equals("adminID"));
              final Date contractYear =
                  (Date) this.getResultColumnValue(res, t -> t.getKey().equals("annoContratto"));
              final String firstName =
                  (String) this.getResultColumnValue(res, t -> t.getKey().equals("nome"));
              final String lastName =
                  (String) this.getResultColumnValue(res, t -> t.getKey().equals("cognome"));
              final int telephone =
                  (int) this.getResultColumnValue(res, t -> t.getKey().equals("telefono"));
              final String email =
                  (String) this.getResultColumnValue(res, t -> t.getKey().equals("email"));
              final String residence =
                  (String) this.getResultColumnValue(res, t -> t.getKey().equals("residenza"));
              admList.add(
                  new Admin(id, contractYear, firstName, lastName, telephone, email, residence));
            });
    return admList;
  }

  private Object getResultColumnValue(
      final List<Pair<String, Object>> res, Predicate<Pair<String, Object>> predicate) {
    return res.stream().filter(t -> predicate.test(t)).findAny().get().getValue();
  }
}
