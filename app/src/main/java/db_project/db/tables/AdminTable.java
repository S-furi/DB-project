package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Admin;
import db_project.utils.Utils;

public class AdminTable extends AbstractTable<Admin, String> {
  public static final String TABLE_NAME = "AMMINISTRATORE";
  public static final String PRIMARY_KEY = "adminID";

  public AdminTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(
        List.of("annoContratto", "nome", "cognome", "telefono", "email", "residenza"));
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
              System.out.println(row.toString());
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
}
