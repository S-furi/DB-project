package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Station;

public class StationTable extends AbstractTable<Station, String>{
  
  public static final String TABLE_NAME = "STAZIONE";
  public static final String PRIMARY_KEY = "codStazione";

  public StationTable(Connection connection){
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("nome", "numBinari", "codResponsabile"));
  }

  @Override
  protected Object[] getSaveQueryParameters(Station station) {
    return new Object[]{
      station.getStationCode(), 
      station.getStationName(), 
      station.getRails(), 
      station.getManagerCode()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(Station station) {
    return new Object[]{
      station.getStationName(), 
      station.getRails(), 
      station.getManagerCode(), 
      station.getStationCode()
    };
  }

  @Override
  protected List<Station> getPrettyResultFromQueryResult(QueryResult result) {
    if(result.getResult().isEmpty()){
      return Collections.emptyList();
    }
    List<Station> stations = new ArrayList<>();
    result.getResult().get().forEach(row -> {
      System.out.println(row.toString());
      final String stationCode = (String) row.get("codStazione");
      final String name = (String) row.get("nome");
      final int numRails = (int) row.get("numBinari");
      final String managerCode = (String) row.get("managerCode");
      stations.add(new Station(stationCode, name, numRails, managerCode));
    });
    return stations;
  }

  

}
