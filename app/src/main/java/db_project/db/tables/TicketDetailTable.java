package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.TicketDetail;

public class TicketDetailTable extends AbstractTable<TicketDetail, String> {
  public static final String TABLE_NAME = "DETTAGLIO_BIGLIETTO";
  public static final String PRIMARY_KEY = "codiceBiglietto";

  public TicketDetailTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(
        List.of("dataPrenotazione", "numClasse", "codTreno", "numeroCarrozza", "numeroPosto"));
  }

  @Override
  protected Object[] getSaveQueryParameters(final TicketDetail ticketDetail) {
    return new Object[] {
      ticketDetail.getTicketId(),
      ticketDetail.getReservationDate(),
      ticketDetail.getTrainClass(),
      ticketDetail.getTrainId(),
      ticketDetail.getCarNumber(),
      ticketDetail.getSeatNumber()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final TicketDetail ticketDetail) {
    return new Object[] {
      ticketDetail.getReservationDate(),
      ticketDetail.getTrainClass(),
      ticketDetail.getTrainId(),
      ticketDetail.getCarNumber(),
      ticketDetail.getSeatNumber(),
      ticketDetail.getTicketId()
    };
  }

  @Override
  public boolean createTable() {
      return false;
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
              System.out.println(row.toString());
              final String ticketId = (String) row.get("codiceBiglietto");
              final Date reservationDate = (Date) row.get("dataPrenotazione");
              final int trainClass = (int) row.get("numClasse");
              final String trainId = (String) row.get("codTreno");
              final int carNumber = (int) row.get("numeroCarrozza");
              final int seatNumber = (int) row.get("numeroPosto");

              ticketDetails.add(
                  new TicketDetail(
                      ticketId, reservationDate, trainClass, trainId, carNumber, seatNumber));
            });
    return ticketDetails;
  }
}
