package db_project.view.adminPanelController;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import db_project.db.dbGenerator.DBGenerator;
import db_project.db.tables.RouteInfoTable;
import db_project.model.RouteInfo;
import db_project.view.adminPanelController.PathController.TripSolution;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class RouteInfoController {
  private final DBGenerator dbGenerator;
  private RouteInfoTable routeInfoTable;

  private final Logger logger;
  private ObservableList<DateTripSolution> routeInfos;

  public RouteInfoController(final DBGenerator dbGenerator) {
    this.dbGenerator = dbGenerator;
    this.routeInfoTable = (RouteInfoTable) this.dbGenerator.getTableByClass(RouteInfoTable.class);

    this.routeInfos = FXCollections.observableArrayList();
    this.logger = Logger.getLogger("RouteInfoController");
    this.logger.setLevel(Level.INFO);
  }

  public boolean saveSelectedPathInfo(final Date date, final String pathId, final String trainId) {
    final RouteInfo routeInfo = new RouteInfo(pathId, trainId, date);

    return this.canRouteInfoBeAdded(routeInfo) && this.routeInfoTable.save(routeInfo);
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
    if (this.getTripSolution(routeInfo.pathId).isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(
        new DateTripSolution(
            routeInfo.getDate(), this.getTripSolution(routeInfo.pathId).get(), routeInfo.trainId));
  }

  /**
   * @param path stringa presa dal menù a tendina nella sezione centrale.
   * @return
   */
  private Optional<TripSolution> getTripSolution(final String path) {
    return new PathController(this.dbGenerator).getTripSolutionFromPathId(path);
  }

  public List<TableColumn<DateTripSolution, ?>> getTableViewColumns() {
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

    final List<TableColumn<DateTripSolution, ?>> lst =
        List.of(dateColumn, srcStationColumn, dstStationColumn, duration, distance, trainIdColumn);

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
    private Date date;
    private String srcStation;
    private String dstStation;
    private String duration;
    private int distance;
    private String trainId;

    public DateTripSolution(
        final Date date, final TripSolution tripSolution, final String trainId) {
      this.date = date;
      this.trainId = trainId;
      srcStation = tripSolution.getSrcStation();
      dstStation = tripSolution.getDstStation();
      duration = tripSolution.getDuration();
      distance = tripSolution.getDistance();
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

    public void setDistance(int distance) {
      this.distance = distance;
    }

    public String getTrainId() {
      return trainId;
    }

    public void setTrainId(String trainId) {
      this.trainId = trainId;
    }

    @Override
    public String toString() {
      return "DateTripSolution [date="
          + date
          + ", distance="
          + distance
          + ", dstStation="
          + dstStation
          + ", duration="
          + duration
          + ", srcStation="
          + srcStation
          + "]";
    }
  }
}
