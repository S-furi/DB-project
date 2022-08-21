package db_project.view.adminPanelController;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import db_project.db.dbGenerator.DBGenerator;
import db_project.db.tables.RouteInfoTable;
import db_project.db.tables.TrainTable;
import db_project.model.RouteInfo;
import db_project.view.adminPanelController.PathController.TripSolution;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class RouteInfoController {
  private final DBGenerator dbGenerator;
  private RouteInfoTable routeInfoTable;
  private final SectionController sectionController;

  private final Logger logger;
  private ObservableList<DateTripSolution> routeInfos;

  public RouteInfoController(final DBGenerator dbGenerator, final SectionController sectionController) {
    this.dbGenerator = dbGenerator;
    this.routeInfoTable = (RouteInfoTable) this.dbGenerator.getTableByClass(RouteInfoTable.class);
    this.sectionController = sectionController;

    this.routeInfos = FXCollections.observableArrayList();
    this.logger = Logger.getLogger("RouteInfoController");
    this.logger.setLevel(Level.WARNING);
  }

  public boolean saveSelectedPathInfo(final Date date, final String pathId, final String trainId) {
    
    final RouteInfo routeInfo = new RouteInfo(pathId, trainId, date, this.getDuration(pathId, this.isTrainRv(trainId)), this.getAvailableSeats(trainId));

    return this.canRouteInfoBeAdded(routeInfo) && this.routeInfoTable.save(routeInfo);
  }

  private int getAvailableSeats(final String trainId) {
    return ((TrainTable) this.dbGenerator.getTableByClass(TrainTable.class)).findByPrimaryKey(trainId).get()
        .getCapacity();
  }

  private boolean isTrainRv(final String trainId) {
    return ((TrainTable)this.dbGenerator.getTableByClass(TrainTable.class)).findByPrimaryKey(trainId).get().getIsRv();
  }

  private String getDuration(final String pathId, final boolean isRv) {
    final int distance = this.sectionController.getTotalDistanceFromPathId(pathId);

    return isRv 
        ? SectionController.getRVDurationFromDistance(distance)
        : SectionController.getStdDurationFromDistance(distance);
  }

  private boolean canRouteInfoBeAdded(final RouteInfo routeInfo) {
    // Vincolo inespresso di un treno che può effettuare solo
    // una tratta al giorno.

    return this.routeInfoTable.findAll().stream()
        .filter(t -> t.getDate().equals(routeInfo.getDate()))
        .filter(t -> t.getTrainId().equals(routeInfo.getTrainId()))
        .findAny()
        .isEmpty();
  }

  public List<Optional<DateTripSolution>> getAllDateTripSolutions() {
    return this.routeInfoTable.findAll().stream()
        .map(t -> this.getDateTripSolutionsFromRouteInfo(t))
        .collect(Collectors.toList());
  }

  private Optional<DateTripSolution> getDateTripSolutionsFromRouteInfo(final RouteInfo routeInfo) {
    if (this.getTripSolution(routeInfo.getPathId()).isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(
        new DateTripSolution(
            routeInfo,
            this.getTripSolution(routeInfo.getPathId()).get(),
            ((TrainTable) this.dbGenerator.getTableByClass(TrainTable.class))
                .findByPrimaryKey(routeInfo.getTrainId())
                .get()
                .getIsRv()));
  }

  /**
   * @param path stringa presa dal menù a tendina nella sezione centrale.
   * @return
   */
  private Optional<TripSolution> getTripSolution(final String path) {
    return new PathController(this.dbGenerator).getTripSolutionFromPathId(path);
  }

  public List<TableColumn<DateTripSolution, ?>> getTableViewColumns() {
    TableColumn<DateTripSolution, String> pathIdColumn = new TableColumn<>("Cod Percorso");
    pathIdColumn.setCellValueFactory(new PropertyValueFactory<>("pathId"));
    TableColumn<DateTripSolution, Date> dateColumn = new TableColumn<>("Data");
    dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    TableColumn<DateTripSolution, String> srcStationColumn = new TableColumn<>("Partenza");
    srcStationColumn.setCellValueFactory(new PropertyValueFactory<>("srcStation"));
    TableColumn<DateTripSolution, String> dstStationColumn = new TableColumn<>("Arrivo");
    dstStationColumn.setCellValueFactory(new PropertyValueFactory<>("dstStation"));
    TableColumn<DateTripSolution, String> duration = new TableColumn<>("Durata");
    duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
    TableColumn<DateTripSolution, Integer> distance = new TableColumn<>("Distanza");
    distance.setCellValueFactory(new PropertyValueFactory<>("distance"));
    TableColumn<DateTripSolution, String> trainIdColumn = new TableColumn<>("Codice Treno");
    trainIdColumn.setCellValueFactory(new PropertyValueFactory<>("trainId"));
    TableColumn<DateTripSolution, String> isRvColumn = new TableColumn<>("RV");
    isRvColumn.setCellValueFactory(new PropertyValueFactory<>("isRv"));
    TableColumn<DateTripSolution, Integer> availableSeatsColumn = new TableColumn<>("Posti Disponibili");
    availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("availableSeats"));

    final List<TableColumn<DateTripSolution, ?>> lst =
        List.of(
            pathIdColumn,
            dateColumn,
            srcStationColumn,
            dstStationColumn,
            duration,
            distance,
            trainIdColumn,
            isRvColumn,
            availableSeatsColumn);

    lst.forEach(t -> t.setStyle("-fx-alignment: CENTER;"));
    return lst;
  }

  public ObservableList<DateTripSolution> getRouteInfos() {
    this.refreshRouteInfos();
    return this.routeInfos;
  }

  public void refreshRouteInfos() {
    this.routeInfos.clear();
    this.routeInfos.addAll(
        this.getAllDateTripSolutions().stream().map(t -> t.get()).collect(Collectors.toList()));
  }

  public class DateTripSolution {
    private String pathId;
    private Date date;
    private String srcStation;
    private String dstStation;
    private String duration;
    private int distance;
    private String trainId;
    private boolean isRv;
    private int availableSeats;

    public DateTripSolution(
      final RouteInfo routeInfo,
      final TripSolution tripSolution,
      final boolean isRv) {
        this.pathId = routeInfo.getPathId();
        this.date = routeInfo.getDate();
        this.srcStation = tripSolution.getSrcStation();
        this.dstStation = tripSolution.getDstStation();
        this.duration = routeInfo.getActualDuration();
        this.distance = tripSolution.getDistance();
        this.trainId = routeInfo.getTrainId();
        this.isRv = isRv;
        this.availableSeats = routeInfo.getAvailableSeats();
      }

    public Date getDate() {
      return date;
    }

    public void setDate(Date date) {
      this.date = date;
    }

    public String getSrcStation() {
      return srcStation;
    }

    public void setSrcStation(String srcStation) {
      this.srcStation = srcStation;
    }

    public String getDstStation() {
      return dstStation;
    }

    public void setDstStation(String dstStation) {
      this.dstStation = dstStation;
    }

    public String getDuration() {
      return duration;
    }

    public void setDuration(String duration) {
      this.duration = duration;
    }

    public int getDistance() {
      return distance;
    }

    public int getAvailableSeats() {
      return availableSeats;
    }

    public void setDistance(int distance) {
      this.distance = distance;
    }

    public String getTrainId() {
      return trainId;
    }

    public void setTrainId(String trainId) {
      this.trainId = trainId;
    }

    public String getPathId() {
      return pathId;
    }

    public void setPathId(String pathId) {
      this.pathId = pathId;
    }

    public boolean getIsRv() {
      return isRv;
    }

    public void setRv(boolean isRv) {
      this.isRv = isRv;
    }

    @Override
    public String toString() {
      return "DateTripSolution [availableSeats=" + availableSeats + ", date=" + date + ", distance=" + distance + ", dstStation="
          + dstStation + ", duration=" + duration + ", isRv=" + isRv + ", pathId=" + pathId + ", srcStation="
          + srcStation + ", trainId=" + trainId + "]";
    }

    
  }
}
