package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import db_project.db.AbstractTable;
import db_project.db.JsonReadeable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Station;
import db_project.utils.AbstractJsonReader;

public class StationTable extends AbstractTable<Station, String> implements JsonReadeable<Station> {
  public static final String TABLE_NAME = "STAZIONE";
  public static final String PRIMARY_KEY = "codStazione";
  private final Logger logger;

  public StationTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("nome", "numBinari", "locazione", "codResponsabile"));
    this.logger = Logger.getLogger("CityTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(final Station station) {
    return new Object[] {
      station.getStationCode(),
      station.getStationName(),
      station.getRails(),
      station.getLocation(),
      station.getManagerCode()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Station station) {
    return new Object[] {
      station.getStationName(),
      station.getRails(),
      station.getLocation(),
      station.getManagerCode(),
      station.getStationCode()
    };
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table STAZIONE ( "
            + "codStazione varchar(10) not null, "
            + "nome varchar(20) not null, "
            + "numBinari int not null, "
            + "locazione varchar(40) not null,"
            + "codResponsabile varchar(5) not null, "
            + "constraint ID_STAZIONE_ID primary key (codStazione)); ";

    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected List<Station> getPrettyResultFromQueryResult(QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<Station> stations = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              logger.info(row.toString());
              final String stationCode = (String) row.get("codStazione");
              final String stationName = (String) row.get("nome");
              final int rails = (int) row.get("numBinari");
              final String location = (String) row.get("locazione");
              final String managerCode = (String) row.get("codResponsabile");
              stations.add(new Station(stationCode, stationName, rails, managerCode, location));
            });
    return stations;
  }

  @Override
  public List<Station> readFromFile() {
    return new AbstractJsonReader<Station>() {}.setFileName("DbStations.json")
        .retreiveData(Station.class);
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
