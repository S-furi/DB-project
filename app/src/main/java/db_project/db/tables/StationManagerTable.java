package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import db_project.db.AbstractTable;
import db_project.db.JsonReadeable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.StationManager;
import db_project.utils.AbstractJsonReader;

public class StationManagerTable extends AbstractTable<StationManager, String>
    implements JsonReadeable<StationManager> {
  public static final String TABLE_NAME = "RESPONSABILE_STAZIONE";
  public static final String PRIMARY_KEY = "codResponsabile";
  private final Logger logger;

  public StationManagerTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(
        List.of("annoContratto", "nome", "cognome", "telefono", "email", "residenza"));

    this.logger = Logger.getLogger("CityTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(StationManager stationManager) {
    return new Object[] {
      stationManager.getManagerCode(),
      stationManager.getContractYear(),
      stationManager.getFirstName(),
      stationManager.getLastName(),
      stationManager.getPhone(),
      stationManager.getEmail(),
      stationManager.getResidence()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(StationManager stationManager) {
    return new Object[] {
      stationManager.getContractYear(),
      stationManager.getFirstName(),
      stationManager.getLastName(),
      stationManager.getPhone(),
      stationManager.getEmail(),
      stationManager.getResidence(),
      stationManager.getManagerCode()
    };
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table RESPONSABILE_STAZIONE ( "
            + "codResponsabile varchar(5) not null, "
            + "annoContratto date not null, "
            + "nome varchar(40) not null, "
            + "cognome varchar(40) not null, "
            + "telefono varchar(10) not null, "
            + "email varchar(50) not null, "
            + "residenza varchar(40) not null, "
            + "constraint ID_RESPONSABILE_STAZIONE_ID primary key (codResponsabile)); ";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected List<StationManager> getPrettyResultFromQueryResult(QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<StationManager> stationManagers = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              logger.info(row.toString());
              final String managerCode = (String) row.get("codResponsabile");
              final Date contractYear = (Date) row.get("annoContratto");
              final String firstName = (String) row.get("nome");
              final String lastName = (String) row.get("cognome");
              final String phone = (String) row.get("telefono");
              final String email = (String) row.get("email");
              final String residence = (String) row.get("residenza");
              stationManagers.add(
                  new StationManager(
                      managerCode, contractYear, firstName, lastName, phone, email, residence));
            });
    return stationManagers;
  }

  @Override
  public List<StationManager> readFromFile() {
    return new AbstractJsonReader<StationManager>() {}.setFileName("DbManagers.json")
        .retreiveData(StationManager.class);
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
