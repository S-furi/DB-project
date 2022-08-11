package db_project.db.tables;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

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
  protected Object[] getSaveQueryParameters(final Train train) {
    return new Object[] {
      train.getTrainCode(),
      train.getLicenseNumber(),
      train.getCapacity(),
      train.isRv() ? "1" : "0"
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Train train) {
    return new Object[] {
      train.getLicenseNumber(),
      train.getCapacity(),
      train.isRv() ? "1" : "0",
      train.getTrainCode()
    };
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table TRENO ( "
            + "codTreno varchar(5) not null, "
            + "codMacchinista varchar(5) not null, "
            + "capienza int not null, "
            + "regionaleVeloce char, "
            + "constraint ID_TRENO_ID primary key (codTreno), "
            + "constraint FKPilota_ID unique (codMacchinista)); ";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected List<Train> getPrettyResultFromQueryResult(final QueryResult result) {
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
              final boolean isRv =
                  ((String) row.get("regionaleVeloce")).equals("0") ? false : true;
              trains.add(new Train(trainCode, licenseNumber, capacity, isRv));
            });
    return trains;
  }
}
