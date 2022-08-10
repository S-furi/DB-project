package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Train;

public class TrainTable extends AbstractTable<Train, String> {

  public static final String TABLE_NAME = "TRENO";
  public static final String PRIMARY_KEY = "codTreno";

  public TrainTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("codMacchinista", "capienza", "regionaleVeloce"));
  }

  @Override
  protected Object[] getSaveQueryParameters(Train train) {
    return new Object[] {
      train.getTrainCode(), train.getLicenseNumber(), train.getCapacity(), train.isRegionaleVeloce()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(Train train) {
    return new Object[] {
      train.getLicenseNumber(), train.getCapacity(), train.isRegionaleVeloce(), train.getTrainCode()
    };
  }

  @Override
  protected List<Train> getPrettyResultFromQueryResult(QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<Train> trains = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              System.out.println(row.toString());
              final String trainCode = (String) row.get("codTreno");
              final String licenseNumber = (String) row.get("codMacchinista");
              final int capacity = (int) row.get("capienza");
              final String isRegionaleVeloce = (String) row.get("regionaleVeloce");
              trains.add(new Train(trainCode, licenseNumber, capacity, isRegionaleVeloce));
            });
    return trains;
  }
}
