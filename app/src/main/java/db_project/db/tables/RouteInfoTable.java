package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import db_project.db.AbstractCompositeKeyTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.RouteInfo;

public class RouteInfoTable extends AbstractCompositeKeyTable<RouteInfo, Object> {
  public static final String TABLE_NAME = "PERCORRENZA";
  public static final List<String> PRIMARY_KEYS = List.of("codPercorso", "codTreno", "data");
  private final Logger logger;

  public RouteInfoTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEYS);
    super.setTableColumns(List.of("tempoEffettivo", "postiDisponibili"));
    this.logger = Logger.getLogger("CityTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(final RouteInfo routeInfo) {
    return new Object[] {
      routeInfo.getPathId(),
      routeInfo.getTrainId(),
      routeInfo.getDate(),
      routeInfo.getActualDuration(),
      routeInfo.getAvailableSeats()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final RouteInfo routeInfo) {
    return new Object[] {
      routeInfo.getPathId(),
      routeInfo.getTrainId(),
      routeInfo.getDate(),
      routeInfo.getActualDuration(),
      routeInfo.getAvailableSeats(),
      routeInfo.getPathId(),
      routeInfo.getTrainId(),
      routeInfo.getDate()
    };
  }

  @Override
  protected List<RouteInfo> getPrettyResultFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<RouteInfo> routeInfos = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              logger.info(row.toString());
              final String pathId = (String) row.get("codPercorso");
              final String trainId = (String) row.get("codTreno");
              final Date date = (Date) row.get("data");
              final String actualDuration = (String) row.get("tempoEffettivo");
              final int availableSeats = (int) row.get("postiDisponibili");
              routeInfos.add(new RouteInfo(pathId, trainId, date, actualDuration, availableSeats));
            });
    return routeInfos;
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table PERCORRENZA ( "
            + "codPercorso varchar(5) not null, "
            + "codTreno varchar(5) not null, "
            + "data date not null, "
            + "tempoEffettivo varchar(10) not null, "
            + "postiDisponibili int not null, "
            + "constraint ID_PERCORRENZA_ID primary key (codPercorso, codTreno, data)); ";

    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  public void updateSeatsTicketBought(final RouteInfo routeInfo) {
    super.update(
        new RouteInfo(
            routeInfo.getPathId(),
            routeInfo.getTrainId(),
            routeInfo.getDate(),
            routeInfo.getActualDuration(),
            routeInfo.getAvailableSeats() - 1));
  }
}
