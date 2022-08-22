package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;

import db_project.db.AbstractCompositeKeyTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.RouteInfo;
import db_project.model.TicketDetail;

public class TicketDetailTable extends AbstractCompositeKeyTable<TicketDetail, Object> {
  public static final String TABLE_NAME = "DETTAGLIO_BIGLIETTO";
  public static final List<String> PRIMARY_KEY =
      List.of("numClasse", "codTreno", "numeroCarrozza", "numeroPosto", "dataViaggio");
  private final Logger logger;

  public TicketDetailTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(List.of("codiceBiglietto"));
    this.logger = Logger.getLogger("TicketDetailTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table DETTAGLIO_BIGLIETTO ( numClasse varchar(1) not null, codTreno varchar(5) not"
            + " null, numeroCarrozza int not null, numeroPosto int not null, dataViaggio date not"
            + " null, codiceBiglietto varchar(5) not null, constraint ID_Prenotazione_ID primary"
            + " key (numClasse, codTreno, numeroCarrozza, numeroPosto, dataViaggio), constraint"
            + " FKRiseva_ID unique (codiceBiglietto)); ";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected Object[] getSaveQueryParameters(final TicketDetail ticketDetail) {
    return new Object[] {
      ticketDetail.getTrainClass(),
      ticketDetail.getTrainId(),
      ticketDetail.getCarNumber(),
      ticketDetail.getSeatNumber(),
      ticketDetail.getTripDate(),
      ticketDetail.getTicketId()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final TicketDetail ticketDetail) {
    return new Object[] {
      ticketDetail.getTrainClass(),
      ticketDetail.getTrainId(),
      ticketDetail.getCarNumber(),
      ticketDetail.getSeatNumber(),
      ticketDetail.getTripDate(),
      ticketDetail.getTicketId(),
      ticketDetail.getTrainClass(),
      ticketDetail.getTrainId(),
      ticketDetail.getCarNumber(),
      ticketDetail.getSeatNumber(),
      ticketDetail.getTripDate()
    };
  }

  @Override
  protected List<TicketDetail> getPrettyResultFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<TicketDetail> ticketDetails = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              logger.info(row.toString());
              final String trainClass = (String) row.get("numClasse");
              final String trainId = (String) row.get("codTreno");
              final int carNumber = (int) row.get("numeroCarrozza");
              final int seatNumber = (int) row.get("numeroPosto");
              final Date reservationDate = (Date) row.get("dataViaggio");
              final String ticketId = (String) row.get("codiceBiglietto");

              ticketDetails.add(
                  new TicketDetail(
                      ticketId, reservationDate, trainClass, trainId, carNumber, seatNumber));
            });
    return ticketDetails;
  }

  public List<TicketDetail> getTicketDetailFromRouteInfo(
      final RouteInfo routeInfo, final String carClass) {
    final String query =
        "SELECT * from dettaglio_biglietto "
            + "where numClasse = ? and codTreno = ? and dataViaggio = ?; ";
    final Object[] params = {carClass, routeInfo.getTrainId(), routeInfo.getDate()};
    this.parser.computeSqlQuery(query, params);
    return this.getPrettyResultFromQueryResult(this.parser.getQueryResult());
  }

  public Optional<TicketDetail> getTicketDetailFromTicketId(final String ticketId) {
    final String query = "SELECT * from DETTAGLIO_BIGLIETTO where codiceBiglietto =?;";
    final Object[] params = {ticketId};
    super.parser.computeSqlQuery(query, params);
    return this.getPrettyResultFromQueryResult(super.parser.getQueryResult()).stream().findAny();
  }
}
