package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Ticket;

public class TicketTable extends AbstractTable<Ticket, String> {
  public static final String TABLE_NAME = "BIGLIETTO";
  public static final String PRIMARY_KEY = "codiceBiglietto";

  public TicketTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(
        List.of(
            "regionaleVeloce",
            "codComitiva",
            "codPasseggero",
            "codPercorso",
            "codTreno",
            "data",
            "prezzo"));
  }

  @Override
  protected Object[] getSaveQueryParameters(final Ticket ticket) {
    return new Object[] {
      ticket.getTicketId(),
      ticket.isRv() ? "1" : "0",
      ticket.getGroupId(),
      ticket.getPassengerId(),
      ticket.getPathId(),
      ticket.getTrainId(),
      ticket.getDate(),
      ticket.getPrice()
    };
  }

  @Override
  protected Object[] getUpdateQueryParameters(final Ticket ticket) {
    return new Object[] {
      ticket.isRv() ? "1" : "0",
      ticket.getGroupId(),
      ticket.getPassengerId(),
      ticket.getPathId(),
      ticket.getTrainId(),
      ticket.getDate(),
      ticket.getPrice(),
      ticket.getTicketId()
    };
  }

  @Override
  public boolean createTable() {
    final String query =
        "create table BIGLIETTO ( "
            + "codiceBiglietto varchar(5) not null, "
            + "regionaleVeloce char not null, "
            + "codComitiva varchar(5), "
            + "codPasseggero varchar(5) not null, "
            + "codPercorso varchar(5) not null, "
            + "codTreno varchar(5) not null, "
            + "data date not null, "
            + "prezzo float(10) not null, "
            + "constraint ID_BIGLIETTO_ID primary key (codiceBiglietto));";
    super.created = super.parser.computeSqlQuery(query, null);
    return super.isCreated();
  }

  @Override
  protected List<Ticket> getPrettyResultFromQueryResult(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    List<Ticket> tickets = new ArrayList<>();
    result
        .getResult()
        .get()
        .forEach(
            row -> {
              System.out.println(row.toString());
              final String ticketId = (String) row.get("codiceBiglietto");
              final boolean isRv = row.get("regionaleVeloce").equals("0") ? false : true;
              final Optional<String> groupId =
                  row.get("codComitiva") == null
                      ? Optional.empty()
                      : Optional.of((String) row.get("codComitiva"));
              final String passengerId = (String) row.get("codPasseggero");
              final String pathId = (String) row.get("codPercorso");
              final String trainId = (String) row.get("codTreno");
              final Date date = (Date) row.get("data");
              final Float price = (Float) row.get("prezzo");
              tickets.add(
                  new Ticket(ticketId, isRv, groupId, passengerId, pathId, trainId, date, price));
            });

    return tickets;
  }
}
