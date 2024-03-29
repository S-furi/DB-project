package db_project.db.tables;

import java.sql.Connection;
import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import db_project.db.AbstractTable;
import db_project.db.queryUtils.QueryResult;
import db_project.model.Ticket;

public class TicketTable extends AbstractTable<Ticket, String> {
  public static final String TABLE_NAME = "BIGLIETTO";
  public static final String PRIMARY_KEY = "codiceBiglietto";
  private final Logger logger;

  public TicketTable(final Connection connection) {
    super(TABLE_NAME, connection);
    super.setPrimaryKey(PRIMARY_KEY);
    super.setTableColumns(
        List.of("regionaleVeloce", "codPasseggero", "codPercorso", "codTreno", "data", "prezzo"));
    this.logger = Logger.getLogger("CityTable");
    this.logger.setLevel(Level.WARNING);
  }

  @Override
  protected Object[] getSaveQueryParameters(final Ticket ticket) {
    return new Object[] {
      ticket.getTicketId(),
      ticket.getIsRv() ? "1" : "0",
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
      ticket.getIsRv() ? "1" : "0",
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
              logger.info(row.toString());
              final String ticketId = (String) row.get("codiceBiglietto");
              final boolean getIsRv = row.get("regionaleVeloce").equals("0") ? false : true;
              final String passengerId = (String) row.get("codPasseggero");
              final String pathId = (String) row.get("codPercorso");
              final String trainId = (String) row.get("codTreno");
              final Date date = (Date) row.get("data");
              final Float price = (Float) row.get("prezzo");
              tickets.add(new Ticket(ticketId, getIsRv, passengerId, pathId, trainId, date, price));
            });

    return tickets;
  }

  public QueryResult getAllBoughtTicketAsQueryResult() {
    final String query =
        "SELECT b.codiceBiglietto, b.codPasseggero, b.regionaleVeloce, b.codPercorso, "
            + "db.numClasse, db.numeroCarrozza, db.numeroPosto, b.data, b.prezzo "
            + "from BIGLIETTO b left join DETTAGLIO_BIGLIETTO db  "
            + "on (b.codiceBiglietto = db.codiceBiglietto); ";
    super.parser.computeSqlQuery(query, null);
    return this.parser.getQueryResult();
  }

  public int getTicketsSoldByMonth(final int month) {
    final String query =
        "SELECT COUNT(prezzo) as Prezzo "
            + "FROM BIGLIETTO "
            + "WHERE MONTH(data) = ? AND YEAR(data) = YEAR(NOW()); ";
    final Object[] params = {month};
    super.parser.computeSqlQuery(query, params);
    final var res = super.parser.getQueryResult().getResult().get().stream().findFirst();
    return ((Long) res.get().get("Prezzo")).intValue();
  }
}
