package db_project.view.userPanelController;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import db_project.db.dbGenerator.DBGenerator;
import db_project.db.tables.RouteInfoTable;
import db_project.db.tables.SeatTable;
import db_project.db.tables.SubscriptionTable;
import db_project.db.tables.TicketDetailTable;
import db_project.db.tables.TicketTable;
import db_project.db.tables.TrainTable;
import db_project.model.RouteInfo;
import db_project.model.Seat;
import db_project.model.Ticket;
import db_project.model.TicketDetail;
import db_project.view.adminPanelController.PathController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

// By now, groups are not handled.
public class TicketBuyController {
  private static final Float KM_FEE = 0.09f;
  private ObservableList<TicketCheckout> tickets;
  private final DBGenerator dbGenerator;
  private TicketTable ticketTable;
  private TicketDetailTable ticketDetailTable;
  private RouteInfoTable routeInfoTable;
  private SeatTable seatTable;
  private final Logger logger;

  public TicketBuyController(DBGenerator dbGenerator) {
    this.dbGenerator = dbGenerator;
    this.initializeTables();
    this.tickets = FXCollections.observableArrayList();
    this.logger = Logger.getLogger("TicketController");
    this.logger.setLevel(Level.WARNING);
  }

  private void initializeTables() {
    this.ticketTable = (TicketTable) this.dbGenerator.getTableByClass(TicketTable.class);
    this.ticketDetailTable =
        (TicketDetailTable) this.dbGenerator.getTableByClass(TicketDetailTable.class);
    this.routeInfoTable = (RouteInfoTable) this.dbGenerator.getTableByClass(RouteInfoTable.class);
    this.seatTable = (SeatTable) this.dbGenerator.getTableByClass(SeatTable.class);
  }

  public boolean registerTicketBought(
      final Date date, final String pathId, final String trainId, final boolean isFirstClass) {

    final boolean isRv = this.checkTrainRv(trainId);

    return isRv
        ? this.saveRvTicket(date, pathId, trainId, isFirstClass)
        : this.saveStdTicket(date, pathId, trainId);
  }

  private boolean saveStdTicket(final Date date, final String pathId, final String trainId) {
    final var routeInfo = this.getRouteInfo(pathId, trainId, date);
    if (routeInfo.isEmpty()) {
      return false;
    }

    // temporary;
    final var usrId = this.getRandomUserId();
    final Float price = this.computePrice(usrId, pathId);

    final Ticket ticket =
        new Ticket(this.getLastTicketId(), false, Optional.empty(), usrId, price, routeInfo.get());

    this.tickets.clear();
    this.tickets.add(new TicketCheckout(ticket.getTicketId(), ticket.getIsRv(), pathId, date));
    this.logger.info(ticket.toString());
    return this.ticketTable.save(ticket);
  }

  private boolean saveRvTicket(
      final Date date, final String pathId, final String trainId, final boolean isFirstClass) {

    final var routeInfo = this.getRouteInfo(pathId, trainId, date);
    if (routeInfo.isEmpty()) {
      return false;
    }
    // tmp
    final var usrId = this.getRandomUserId();
    final Float price = this.computePrice(usrId, pathId) * 1.15f;

    final Ticket ticket =
        new Ticket(this.getLastTicketId(), true, Optional.empty(), usrId, price, routeInfo.get());
    final Optional<TicketDetail> ticketDetail =
        this.buildTicketDetail(ticket, routeInfo.get(), isFirstClass);

    if (ticketDetail.isEmpty()) {
      return false;
    }

    this.logger.info(ticket.toString());
    this.logger.info(ticketDetail.toString());

    this.tickets.clear();
    this.tickets.add(
        new TicketCheckout(
            ticket.getTicketId(),
            true,
            routeInfo.get().getPathId(),
            ticketDetail.get().getTrainClass(),
            ticketDetail.get().getCarNumber(),
            ticketDetail.get().getSeatNumber(),
            ticketDetail.get().getTripDate()));

    return this.ticketTable.save(ticket) && this.ticketDetailTable.save(ticketDetail.get());
  }

  private Optional<TicketDetail> buildTicketDetail(
      final Ticket ticket, final RouteInfo routeInfo, final boolean isFirstClass) {
    final var seat = this.getFirstAvailableSeat(routeInfo, isFirstClass ? "1" : "2");
    if (seat.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(
        TicketDetail.getTicketDetailFromSeat(
            ticket.getTicketId(), routeInfo.getDate(), seat.get()));
  }

  private Optional<Seat> getFirstAvailableSeat(final RouteInfo routeInfo, final String carClass) {
    if (this.ticketDetailTable.getTicketDetailFromRouteInfo(routeInfo, carClass).isEmpty()) {
      return this.seatTable.getFirstSeat(routeInfo, carClass);
    }
    return this.seatTable.getFirstAvailableSeat(routeInfo, carClass);
  }

  private Float computePrice(final String usrId, final String pathId) {
    final int discount =
        ((SubscriptionTable) this.dbGenerator.getTableByClass(SubscriptionTable.class))
            .getDiscountPassengersPercentages()
            .get(usrId);

    final int totalDistance =
        new PathController(this.dbGenerator).getTripSolutionFromPathId(pathId).get().getDistance();
    return (totalDistance * KM_FEE) - ((totalDistance * KM_FEE) * discount) / 100;
  }

  private Optional<RouteInfo> getRouteInfo(
      final String pathId, final String trainId, final Date date) {
    return this.routeInfoTable.findByPrimaryKey(List.of(pathId, trainId, date));
  }

  private String getLastTicketId() {
    final var id = this.retreiveLastTicketId();
    this.logger.info(this.retreiveLastTicketId().toString());
    if (id.isPresent()) {
      return id.get();
    }
    return "1";
  }

  private Optional<String> retreiveLastTicketId() {
    return this.ticketTable.findAll().stream()
        .map(t -> Integer.parseInt(t.getTicketId()))
        .sorted((t1, t2) -> Integer.compare(t2, t1))
        .map(t -> t + 1)
        .map(String::valueOf)
        .findFirst();
  }

  public List<TableColumn<TicketCheckout, ?>> getTableViewColumns() {
    final TableColumn<TicketCheckout, String> ticketIdColumn =
        new TableColumn<>("Numero Biglietto");
    ticketIdColumn.setCellValueFactory(new PropertyValueFactory<>("ticketId"));
    final TableColumn<TicketCheckout, Boolean> isRvColumn = new TableColumn<>("RV");
    isRvColumn.setCellValueFactory(new PropertyValueFactory<>("isRv"));
    final TableColumn<TicketCheckout, String> pathIdColumn = new TableColumn<>("Percorso");
    pathIdColumn.setCellValueFactory(new PropertyValueFactory<>("pathId"));
    final TableColumn<TicketCheckout, String> classNumberColumn = new TableColumn<>("Classe");
    classNumberColumn.setCellValueFactory(new PropertyValueFactory<>("classNumber"));
    final TableColumn<TicketCheckout, Integer> carNumberColumn = new TableColumn<>("Carrozza");
    carNumberColumn.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
    final TableColumn<TicketCheckout, Integer> seatNumberColumn = new TableColumn<>("Posto");
    seatNumberColumn.setCellValueFactory(new PropertyValueFactory<>("seatNumber"));
    final TableColumn<TicketCheckout, Date> tripDateColumn = new TableColumn<>("Data Viaggio");
    tripDateColumn.setCellValueFactory(new PropertyValueFactory<>("tripDate"));

    final List<TableColumn<TicketCheckout, ?>> lst =
        List.of(
            ticketIdColumn,
            isRvColumn,
            pathIdColumn,
            classNumberColumn,
            carNumberColumn,
            seatNumberColumn,
            tripDateColumn);

    lst.forEach(t -> t.setStyle("-fx-alignment: CENTER;"));

    return lst;
  }

  public ObservableList<TicketCheckout> getBoughtTicketDetails() {
    return this.tickets;
  }

  public void restoreLastOp() {
    if (this.tickets.isEmpty()) {
      return;
    }
    final var ticket = this.tickets.get(0);
    if (ticket.getIsRv()) {
      if (this.ticketDetailTable.getTicketDetailFromTicketId(ticket.getTicketId()).isEmpty()) {
        this.ticketTable.delete(ticket.getTicketId());
      }
    }
  }

  public class TicketCheckout {
    private String ticketId;
    private boolean isRv;
    private String pathId;
    private String classNumber;
    private int carNumber;
    private int seatNumber;
    private Date tripDate;

    public TicketCheckout(
        String ticketId,
        boolean isRv,
        String pathId,
        String classNumber,
        int carNumber,
        int seatNumber,
        Date tripDate) {
      this.ticketId = ticketId;
      this.isRv = isRv;
      this.pathId = pathId;
      this.classNumber = classNumber;
      this.carNumber = carNumber;
      this.seatNumber = seatNumber;
      this.tripDate = tripDate;
    }

    public TicketCheckout(String ticketId, boolean isRv, String pathId, Date tripDate) {
      this.ticketId = ticketId;
      this.isRv = isRv;
      this.pathId = pathId;
      this.classNumber = "None";
      this.carNumber = 0;
      this.seatNumber = 0;
      this.tripDate = tripDate;
    }

    public String getTicketId() {
      return ticketId;
    }

    public boolean getIsRv() {
      return isRv;
    }

    public String getPathId() {
      return pathId;
    }

    public String getClassNumber() {
      return classNumber;
    }

    public int getCarNumber() {
      return carNumber;
    }

    public int getSeatNumber() {
      return seatNumber;
    }

    public Date getTripDate() {
      return tripDate;
    }

    @Override
    public String toString() {
      return "TicketCheckout [carNumber="
          + carNumber
          + ", classNumber="
          + classNumber
          + ", isRv="
          + isRv
          + ", pathId="
          + pathId
          + ", seatNumber="
          + seatNumber
          + ", ticketId="
          + ticketId
          + ", tripDate="
          + tripDate
          + "]";
    }
  }

  // super temp
  private String getRandomUserId() {
    final var lst =
        ((SubscriptionTable) this.dbGenerator.getTableByClass(SubscriptionTable.class))
            .findAll().stream().map(t -> t.getPassengerCode()).collect(Collectors.toList());
    return lst.get(new Random().nextInt(lst.size() - 1));
  }

  public boolean checkTrainRv(final String trainId) {
    if (trainId == null) return false;
    return ((TrainTable) this.dbGenerator.getTableByClass(TrainTable.class))
        .findByPrimaryKey(trainId)
        .get()
        .getIsRv();
  }
}
