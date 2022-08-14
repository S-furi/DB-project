package db_project.db.tables;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import db_project.db.AbstractCompositeKeyTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Seat;

public class SeatTable extends AbstractCompositeKeyTable<Seat, Object> {
  public static final String TABLE_NAME = "POSTO";
  public static final List<String> PRIMARY_KEY = List.of("numClasse", "codTreno", "numeroCarrozza", "numeroPosto");
  private final Logger logger;

  public SeatTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(Collections.emptyList());
    this.logger = Logger.getLogger("SeatTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  public boolean createTable() {
    final String query = "create table POSTO ( " + "numClasse varchar(1) not null, " + "codTreno varchar(5) not null, "
        + "numeroCarrozza int not null, " + "numeroPosto int not null, "
        + "constraint ID_POSTO_ID primary key (numClasse, codTreno, numeroCarrozza, numeroPosto)); ";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected Object[] getSaveQueryParameters(final Seat seat) {
    return new Object[] { seat.getClassType(), seat.getTrainCode(), seat.getCarNumber(), seat.getSeatNumber() };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Seat seat) {
    return new Object[] { seat.getClassType(), seat.getTrainCode(), seat.getCarNumber(), seat.getSeatNumber(),
        seat.getClassType(), seat.getTrainCode(), seat.getCarNumber(), seat.getSeatNumber() };
  }

  @Override
  protected List<Seat> getPrettyResultFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<Seat> seats = new ArrayList<>();
    result.getResult().get().forEach(row -> {
      this.logger.info(row.toString());
      final String classNum = (String) row.get("numClasse");
      final String trainCode = (String) row.get("codTreno");
      final int carNumber = (int) row.get("numeroCarrozza");
      final int seatNumber = (int) row.get("numeroPosto");

      seats.add(new Seat(classNum, trainCode, carNumber, seatNumber));
    });
    return seats;
  }
}
