package db_project.db.tables;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Train;

public class TrainTable extends AbstractTable<Train, String> {
  public static final String TABLE_NAME = "TRENO";
  public static final String PRIMARY_KEY = "codTreno";
  private final Logger logger;

  public TrainTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("codMacchinista", "capienza", "regionaleVeloce"));
    this.logger = Logger.getLogger("TrainTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(Train train) {
    return new Object[] {
      train.getTrainCode(),
      train.getLicenseNumber(),
      train.getCapacity(),
      train.getIsRv() ? "1" : "0"
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(Train train) {
    return new Object[] {
      train.getLicenseNumber(),
      train.getCapacity(),
      train.getIsRv() ? "1" : "0",
      train.getTrainCode()
    };
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table TRENO ( "
            + "codTreno varchar(5) not null, "
            + "capienza int not null, "
            + "regionaleVeloce char, "
            + "codMacchinista varchar(5) not null, "
            + "constraint ID_TRENO_ID primary key (codTreno)); ";

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
              final boolean getIsRv = ((String) row.get("regionaleVeloce")).equals("1");
              trains.add(new Train(trainCode, licenseNumber, capacity, getIsRv));
            });
    return trains;
  }
}
