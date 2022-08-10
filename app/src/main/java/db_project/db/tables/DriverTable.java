package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Driver;
import db_project.utils.Utils;

public class DriverTable extends AbstractTable<Driver, String> {
  public static final String TABLE_NAME = "MACCHINISTA";
  public static final String PRIMARY_KEY = "numeroPatente";

  public DriverTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(
        List.of("annoContratto", "nome", "cognome", "telefono", "email", "residenza"));
  }

  @Override
  protected Object[] getSaveQueryParameters(final Driver driver) {
    return new Object[] {
      driver.getLicenceNumber(),
      Utils.dateToSqlDate(driver.getContractYear()),
      driver.getFirstName(),
      driver.getLastName(),
      driver.getTelephone(),
      driver.getEmail(),
      driver.getResidence()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Driver driver) {
    return new Object[] {
      Utils.dateToSqlDate(driver.getContractYear()),
      driver.getFirstName(),
      driver.getLastName(),
      driver.getTelephone(),
      driver.getEmail(),
      driver.getResidence(),
      driver.getLicenceNumber()
    };
  }

  @Override
  public boolean createTable() {
      return false;
  }

  @Override
  protected List<Driver> getPrettyResultFromQueryResult(QueryResult result) {
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
              final String telephone = (String) row.get("telefono");
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
