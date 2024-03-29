package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import db_project.db.AbstractTable;
import db_project.db.JsonReadeable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Admin;
import db_project.utils.AbstractJsonReader;
import db_project.utils.Utils;

public class AdminTable extends AbstractTable<Admin, String> implements JsonReadeable<Admin> {
  public static final String TABLE_NAME = "AMMINISTRATORE";
  public static final String PRIMARY_KEY = "adminID";
  private final Logger logger;

  public AdminTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(
        List.of("annoContratto", "nome", "cognome", "telefono", "email", "residenza"));
    this.logger = Logger.getLogger("AdminTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(final Admin admin) {
    return new Object[] {
      admin.getId(),
      Utils.dateToSqlDate(admin.getContractYear()),
      admin.getFirstName(),
      admin.getLastName(),
      admin.getTelephone(),
      admin.getEmail(),
      admin.getResidence()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Admin admin) {
    return new Object[] {
      Utils.dateToSqlDate(admin.getContractYear()),
      admin.getFirstName(),
      admin.getLastName(),
      admin.getTelephone(),
      admin.getEmail(),
      admin.getResidence(),
      admin.getId()
    };
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table AMMINISTRATORE ( "
            + "adminID varchar(5) not null, "
            + "annoContratto date not null, "
            + "nome varchar(40) not null, "
            + "cognome varchar(40) not null, "
            + "telefono varchar(10) not null, "
            + "email varchar(50) not null, "
            + "residenza varchar(40) not null, "
            + "constraint ID_AMMINISTRATORE_ID primary key (adminID));";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.created;
  }

  @Override
  protected List<Admin> getPrettyResultFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    final List<Admin> admList = new ArrayList<>();

    result
        .getResult()
        .get()
        .forEach(
            row -> {
              logger.info(row.toString());
              final String id = (String) row.get("adminID");
              final Date contractYear = (Date) row.get("annoContratto");
              final String firstName = (String) row.get("nome");
              final String lastName = (String) row.get("cognome");
              final String telephone = (String) row.get("telefono");
              final String email = (String) row.get("email");
              final String residence = (String) row.get("residenza");
              admList.add(
                  new Admin(id, contractYear, firstName, lastName, telephone, email, residence));
            });

    return admList;
  }

  public String getRandomAdminId() {
    final var admins = this.findAll();
    return admins.get(new Random().nextInt(admins.size() - 1)).getId();
  }

  @Override
  public List<Admin> readFromFile() {
    return new AbstractJsonReader<Admin>() {}.setFileName("DbAdmins.json")
        .retreiveData(Admin.class);
  }

  public int getHighestID() {
    final String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY adminId DESC LIMIT 1";
    super.parser.computeSqlQuery(query, null);
    var admin = this.getPrettyResultFromQueryResult(super.parser.getQueryResult());
    return admin.isEmpty() ? 0 : Integer.parseInt(admin.get(0).getId());
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
