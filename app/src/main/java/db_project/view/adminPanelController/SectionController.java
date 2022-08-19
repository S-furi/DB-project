package db_project.view.adminPanelController;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import db_project.db.dbGenerator.DBGenerator;
import db_project.db.queryUtils.ArrayQueryParser;
import db_project.db.queryUtils.QueryParser;
import db_project.db.queryUtils.QueryResult;
import db_project.db.tables.AdminTable;
import db_project.db.tables.PathInfoTable;
import db_project.db.tables.PathTable;
import db_project.db.tables.SectionTable;
import db_project.model.Path;
import db_project.model.PathInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class SectionController {
  private static final double AVG_STD_TRAIN_SPEED = 130.0;

  private final DBGenerator dbGenerator;
  private SectionTable sectionTable;
  private PathTable pathTable;
  private PathInfoTable pathInfoTable;
  private final PathController pathController;
  private ObservableList<PathDetail> pathDetails;
  private final QueryParser parser;
  private final Logger logger;
  
  private Path computedPath;
  private List<PathInfo> pathInfosToBeSaved = new ArrayList<>();

  public SectionController(final DBGenerator dbGenerator, final PathController pathController) {
    this.dbGenerator = dbGenerator;
    this.pathController = pathController;
    this.initializeTables();

    this.parser =
        new ArrayQueryParser(this.dbGenerator.getConnectionProvider().getMySQLConnection());

    this.pathDetails = FXCollections.observableArrayList();

    this.logger = Logger.getLogger("SectionController");
    this.logger.setLevel(Level.WARNING);
  }

  private void initializeTables() {
    this.sectionTable = (SectionTable) this.dbGenerator.getTableByClass(SectionTable.class);
    this.pathTable = (PathTable) this.dbGenerator.getTableByClass(PathTable.class);
    this.pathInfoTable = (PathInfoTable) this.dbGenerator.getTableByClass(PathInfoTable.class);
  }

  /**
   * 
   * @param path that already exists in DB!!!
   */
  public void computeSectionsFromPath(final Path path) {
    final var details = this.retreiveSections(path);
    if (details.isEmpty()) {
      return;
    }
    this.pathDetails.addAll(
        details.get().stream()
            .sorted((t1, t2) -> Integer.compare(t1.getOrder(), t2.getOrder()))
            .collect(Collectors.toList()));
  }

  private Optional<List<PathDetail>> retreiveSections(final Path path) {
    final String query =
        "SELECT p.codPercorso, dp.ordine, t.codTratta, Partenza.nome as StazionePartenza,"
            + " Arrivo.nome as StazioneArrivo from ( select codTratta, nome from tratta, stazione"
            + " where codStazioneArrivo = codStazione) as Arrivo, ( select codTratta, nome from"
            + " tratta, stazione where codStazionePartenza = codStazione) as Partenza, percorso p,"
            + " dettaglio_percorso dp, tratta t where t.codTratta = Arrivo.codTratta and"
            + " t.codTratta = Partenza.codTratta and t.codTratta = dp.codTratta and dp.codPercorso"
            + " = p.codPercorso and p.codPercorso = ? GROUP BY dp.ordine, p.codPercorso,"
            + " t.codTratta, StazionePartenza, StazioneArrivo order by dp.ordine; ";
    this.parser.computeSqlQuery(query, new Object[] {path.getPathCode()});
    return Optional.of(this.getPathDetailsFromQuery(this.parser.getQueryResult()));
  }

  /**
   * 
   * @param pathId that isn't contained into the database
   * @return
   */
  public boolean findSolution(final String pathId) {
    if (this.isPathAlreadyInDb(pathId)) {
      return false;
    }

    final List<PathInfo> pathInfos = TripSolutionFinder.getPathInfosFromPathId(pathId);
    if (pathInfos.isEmpty()) {
      return false;
    }
    this.pathInfosToBeSaved = pathInfos;

    return this.buildPathDetailsFromPathInfos(pathInfos);
  }

  private boolean isPathAlreadyInDb(final String pathId) {
    return this.pathTable.findByPrimaryKey(pathId).isPresent();
  }

  private boolean buildPathDetailsFromPathInfos(final List<PathInfo> pathInfos) {
    final String pathId = pathInfos.get(0).getPathId();

    final List<String> sectionIds = 
        pathInfos.stream().map(t -> t.getSectionId()).collect(Collectors.toList());

    final int totalDistance = this.sectionTable.findAll()
        .stream()
        .filter(t -> sectionIds.contains(t.getSectionCode()))
        .map(t -> t.getDistance())
        .reduce(0, (t1, t2) -> t1 + t2);
    
    this.computedPath = new Path(pathId, this.getDurationFromDistance(totalDistance), sectionIds.size(), this.getRandomAdminId());
  
    this.buildNewPathDetail(pathId, pathInfos);

    return !this.pathDetails.isEmpty();
  }

  private void buildNewPathDetail(String pathId, List<PathInfo> pathInfos) {
    this.pathDetails.clear();
    for (final var pathInfo : pathInfos) {
      var stations = this.pathController.getStationsNameFromPathCode(pathInfo.getSectionId());
      var srcStation = stations.getKey();
      var dstStation = stations.getValue();
      this.pathDetails.add(
          new PathDetail(pathId, 
              Integer.parseInt(pathInfo.getOrderNumber()), 
              pathInfo.getSectionId(), 
              srcStation, 
              dstStation));
    }
  }

  private String getRandomAdminId() {
    return new AdminTable(this.dbGenerator.getConnectionProvider().getMySQLConnection()).getRandomAdminId();
  }

  private String getDurationFromDistance(final int distance) { 
    return this.getDurationFromMinutes((int)((distance/AVG_STD_TRAIN_SPEED)*60));
  }

  private String getDurationFromMinutes(final long value) {
    return LocalTime.MIN.plus(
        Duration
        .ofMinutes(value))
        .toString();
}


  private List<PathDetail> getPathDetailsFromQuery(final QueryResult result) {
    if (result.getResult().isEmpty()) {
      return Collections.emptyList();
    }
    final List<PathDetail> pathDetails = new ArrayList<>();

    result
        .getResult()
        .get()
        .forEach(
            row -> {
              this.logger.info(row.toString());
              final String pathId = (String) row.get("codPercorso");
              final int order = Integer.valueOf((String) row.get("ordine"));
              final String sectionId = (String) row.get("codTratta");
              final String srcStationName = (String) row.get("StazionePartenza");
              final String dstStationName = (String) row.get("StazioneArrivo");
              pathDetails.add(
                  new PathDetail(pathId, order, sectionId, srcStationName, dstStationName));
            });
    return pathDetails;
  }

  public List<TableColumn<PathDetail, ?>> getTableViewColumns() {
    TableColumn<PathDetail, String> pathIdColumn = new TableColumn<>("CodPercorso");
    pathIdColumn.setCellValueFactory(new PropertyValueFactory<>("pathId"));
    TableColumn<PathDetail, Integer> orderColumn = new TableColumn<>("Ordine");
    orderColumn.setCellValueFactory(new PropertyValueFactory<>("order"));
    TableColumn<PathDetail, String> sectionIdColumn = new TableColumn<>("CodTratta");
    sectionIdColumn.setCellValueFactory(new PropertyValueFactory<>("sectionId"));
    TableColumn<PathDetail, String> srcStationColumn = new TableColumn<>("Partenza");
    srcStationColumn.setCellValueFactory(new PropertyValueFactory<>("srcStationName"));
    TableColumn<PathDetail, String> dstStationColumn = new TableColumn<>("Arrivo");
    dstStationColumn.setCellValueFactory(new PropertyValueFactory<>("dstStationName"));

    final List<TableColumn<PathDetail, ?>> lst =
        List.of(pathIdColumn, orderColumn, sectionIdColumn, srcStationColumn, dstStationColumn);

    lst.forEach(t -> t.setStyle("-fx-alignment: CENTER;"));

    return lst;
  }

  public ObservableList<PathDetail> getPathDetails() {
    return this.pathDetails;
  }

  public void clearPathDetails() {
    this.computedPath = null;
    this.pathDetails.clear();
    this.pathInfosToBeSaved.clear();
  }

  public boolean savePathInfosToDb() {
    for (final var pathInfo : this.pathInfosToBeSaved) {
      if (!this.pathInfoTable.save(pathInfo)) {
        return false;
      }
    }
    this.pathInfosToBeSaved.clear();
    return true;
  }

  public Path getComputedPath() {
    return this.computedPath;
  }

  public class PathDetail {
    private String pathId;
    private int order;
    private String sectionId;
    private String srcStationName;
    private String dstStationName;

    public PathDetail(
        String pathId, int order, String sectionId, String srcStationName, String dstStationName) {
      this.pathId = pathId;
      this.order = order;
      this.sectionId = sectionId;
      this.srcStationName = srcStationName;
      this.dstStationName = dstStationName;
    }

    public String getPathId() {
      return pathId;
    }

    public void setPathId(String pathId) {
      this.pathId = pathId;
    }

    public int getOrder() {
      return order;
    }

    public void setOrder(int order) {
      this.order = order;
    }

    public String getSectionId() {
      return sectionId;
    }

    public void setSectionId(String sectionId) {
      this.sectionId = sectionId;
    }

    public String getSrcStationName() {
      return srcStationName;
    }

    public void setSrcStationName(String srcStationName) {
      this.srcStationName = srcStationName;
    }

    public String getDstStationName() {
      return dstStationName;
    }

    public void setDstStationName(String dstStationName) {
      this.dstStationName = dstStationName;
    }

    @Override
    public String toString() {
      return "PathDetail [dstStationName="
          + dstStationName
          + ", order="
          + order
          + ", pathId="
          + pathId
          + ", sectionId="
          + sectionId
          + ", srcStationName="
          + srcStationName
          + "]";
    }
  }
}
