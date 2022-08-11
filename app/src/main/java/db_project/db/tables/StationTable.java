package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Station;

public class StationTable extends AbstractTable<Station, String> {
  public static final String TABLE_NAME = "STAZIONE";
  public static final String PRIMARY_KEY = "codStazione";

  public StationTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("nome", "numBinari", "codResponsabile"));
  }

  @Override
  protected Object[] getSaveQueryParameters(final Station station) {
    return new Object[] {
      station.getStationCode(),
      station.getStationName(),
      station.getRails(),
      station.getManagerCode()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Station station) {
    return new Object[] {
      station.getStationName(),
      station.getRails(),
      station.getManagerCode(),
      station.getStationCode()
    };
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table STAZIONE ( "
            + "codStazione varchar(5) not null, "
            + "nome varchar(20) not null, "
            + "numBinari int not null, "
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
              System.out.println(row.toString());
              final String stationCode = (String) row.get("codStazione");
              final String stationName = (String) row.get("nome");
              final int rails = (int) row.get("numBinari");
              final String managerCode = (String) row.get("codResponsabile");
              stations.add(new Station(stationCode, stationName, rails, managerCode));
            });
    return stations;
  }
}
