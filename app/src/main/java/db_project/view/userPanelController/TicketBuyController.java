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
import db_project.db.tables.SubscriptionTable;
import db_project.db.tables.TicketTable;
import db_project.model.RouteInfo;
import db_project.model.Ticket;
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
    private final TicketTable ticketTable;
    private final RouteInfoTable routeInfoTable;
    private final Logger logger;

	public TicketBuyController(DBGenerator dbGenerator) {
        this.tickets = FXCollections.observableArrayList();
        this.dbGenerator = dbGenerator;
        this.ticketTable = (TicketTable) this.dbGenerator.getTableByClass(TicketTable.class);
        this.routeInfoTable = (RouteInfoTable) this.dbGenerator.getTableByClass(RouteInfoTable.class);
        this.logger = Logger.getLogger("TicketController");
        this.logger.setLevel(Level.INFO);
	}

    public boolean registerTicketBought(final Date date, final String pathId, 
            final String trainId, final boolean isRv,final boolean isFirstClass) {
        
        return isRv ? this.saveRvTicket(date, pathId, trainId, isFirstClass)
            : this.saveStdTicket(date, pathId, trainId);
    }
    
    private boolean saveStdTicket(final Date date, final String pathId, final String trainId) {
        final var routeInfo = this.getRouteInfo(pathId, trainId, date);
        if (routeInfo.isEmpty()) {
            return false;
        }

        //temporary;
        final var usrId = this.getRandomUserId();
        final Float price = this.computePrice(usrId, pathId);

        final Ticket ticket = 
            new Ticket(this.getLastTicketId(), false, Optional.empty(), usrId, price, routeInfo.get());

        this.tickets.clear();
        this.tickets.add(new TicketCheckout(ticket.getTicketId(), ticket.getIsRv(), pathId, date));
        this.logger.info(ticket.toString());
        return this.ticketTable.save(ticket);
    }

    private boolean saveRvTicket(final Date date, final String pathId, final String trainId, final boolean isFirstClass) {
        return false;
    }

    private Float computePrice(final String usrId, final String pathId) {
        final int discount = ((SubscriptionTable)this.dbGenerator.getTableByClass(SubscriptionTable.class))
                .getDiscountPassengersPercentages().get(usrId);
        
        final int totalDistance = new PathController(this.dbGenerator).getTripSolutionFromPathId(pathId).get()
                .getDistance();
        return (totalDistance * KM_FEE) - ((totalDistance * KM_FEE) * discount) / 100;

    }

    private Optional<RouteInfo> getRouteInfo(final String pathId, final String trainId, final Date date) {
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
        return this.ticketTable
            .findAll()
            .stream()
            .map(t -> Integer.parseInt(t.getTicketId()))
            .sorted((t1, t2) -> Integer.compare(t2, t1))
            .map(t -> t+1)
            .map(String::valueOf)
            .findFirst();
    }

    public List<TableColumn<TicketCheckout, ?>> getTableViewColumns() {
        final TableColumn<TicketCheckout, String> ticketIdColumn = new TableColumn<>("Numero Biglietto");
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

        final List<TableColumn<TicketCheckout, ?>> lst = List.of(
            ticketIdColumn,
            isRvColumn,
            pathIdColumn,
            classNumberColumn,
            carNumberColumn,
            seatNumberColumn,
            tripDateColumn
        );

        lst.forEach(t -> t.setStyle("-fx-alignment: CENTER;"));

        return lst;
    }

    public ObservableList<TicketCheckout> getBoughtTicketDetails() {
        return this.tickets;
    }

    public class TicketCheckout {
        private String ticketId;
        private boolean isRv;
        private String pathId;
        private String classNumber;
        private int carNumber;
        private int seatNumber;
        private Date tripDate;
        
        public TicketCheckout(String ticketId, boolean isRv, String pathId, String classNumber, int carNumber,
                int seatNumber, Date tripDate) {
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
            return "TicketCheckout [carNumber=" + carNumber + ", classNumber=" + classNumber + ", isRv=" + isRv
                    + ", pathId=" + pathId + ", seatNumber=" + seatNumber + ", ticketId=" + ticketId + ", tripDate="
                    + tripDate + "]";
        }
    }   


    //super temp
    private String getRandomUserId() {
        final var lst = ((SubscriptionTable) this.dbGenerator.getTableByClass(SubscriptionTable.class)).findAll()
                .stream().map(t -> t.getPassengerCode()).collect(Collectors.toList());
        return lst.get(new Random().nextInt(lst.size() - 1));
    }
}
