package db_project.db.tables;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import db_project.db.AbstractCompositeKeyTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.RouteInfo;
import db_project.model.Seat;

public class SeatTable extends AbstractCompositeKeyTable<Seat, Object> {
  public static final String TABLE_NAME = "POSTO";
  public static final List<String> PRIMARY_KEY =
      List.of("numClasse", "codTreno", "numeroCarrozza", "numeroPosto");
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
    final String query =
        "create table POSTO ( numClasse varchar(1) not null, codTreno varchar(5) not null,"
            + " numeroCarrozza int not null, numeroPosto int not null, constraint ID_POSTO_ID"
            + " primary key (numClasse, codTreno, numeroCarrozza, numeroPosto)); ";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected Object[] getSaveQueryParameters(final Seat seat) {
    return new Object[] {
      seat.getClassType(), seat.getTrainCode(), seat.getCarNumber(), seat.getSeatNumber()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Seat seat) {
    return new Object[] {
      seat.getClassType(),
      seat.getTrainCode(),
      seat.getCarNumber(),
      seat.getSeatNumber(),
      seat.getClassType(),
      seat.getTrainCode(),
      seat.getCarNumber(),
      seat.getSeatNumber()
    };
  }

  @Override
  protected List<Seat> getPrettyResultFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<Seat> seats = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              this.logger.info(row.toString());
              final String classNum = (String) row.get("numClasse");
              final String trainCode = (String) row.get("codTreno");
              final int carNumber = (int) row.get("numeroCarrozza");
              final int seatNumber = (int) row.get("numeroPosto");

              seats.add(new Seat(classNum, trainCode, carNumber, seatNumber));
            });
    return seats;
  }

  /**
   * Used when there are reserved seats in the given routeInfo.
   *
   * @param routeInfo
   * @param carClass
   * @return
   */
  public Optional<Seat> getFirstAvailableSeat(final RouteInfo routeInfo, final String carClass) {
    final String query =
        "SELECT p.* from (select db2.numClasse, db2.codTreno, db2.numeroCarrozza, db2.numeroPosto "
            + " from dettaglio_biglietto db2 where db2.codTreno = ? and db2.numClasse = ? and"
            + " db2.dataViaggio = ?) db, posto p where p.codTreno = db.codTreno and p.numClasse ="
            + " db.numClasse and (p.numeroCarrozza <> db.numeroCarrozza or p.numeroPosto not in "
            + " (select db3.numeroPosto from dettaglio_biglietto db3)); ";

    final Object[] params = {routeInfo.getTrainId(), carClass, routeInfo.getDate()};
    super.parser.computeSqlQuery(query, params);

    return this.getPrettyResultFromQueryResult(super.parser.getQueryResult()).stream().findFirst();
  }

  /**
   * Used when there are no seats reserved for given RouteInfo.
   *
   * @return
   */
  public Optional<Seat> getFirstSeat(final RouteInfo routeInfo, final String carClass) {
    final String query =
        "SELECT * from posto  "
            + "where codTreno = ? "
            + "and numClasse = ? "
            + "order by numeroCarrozza, numeroPosto; ";
    final Object[] params = {routeInfo.getTrainId(), carClass};
    super.parser.computeSqlQuery(query, params);
    return this.getPrettyResultFromQueryResult(super.parser.getQueryResult()).stream().findFirst();
  }
}
