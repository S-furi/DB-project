package db_project.view;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import db_project.db.dbGenerator.DBGenerator;
import db_project.model.Driver;
import db_project.model.Train;
import db_project.utils.Utils;
import db_project.view.controller.PathController;
import db_project.view.controller.RouteInfoController;
import db_project.view.controller.SectionController;
import db_project.view.controller.TrainController;
import db_project.view.controller.PathController.TripSolution;
import db_project.view.controller.RouteInfoController.DateTripSolution;
import db_project.view.controller.SectionController.PathDetail;
import javafx.fxml.Initializable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class DbRailwayController implements Initializable {
  // First Tab
  @FXML private DatePicker datePicker;
  @FXML private ChoiceBox<String> pathChoiceBox;
  @FXML private ChoiceBox<String> trainChoiceBox;
  @FXML private TableView<DateTripSolution> resultTableView;
  @FXML private Button routeConfirmationButton;
  @FXML private Button routeRefreshButton;

  // Second Tab
  @FXML private ChoiceBox<String> srcStationChoiceBox;
  @FXML private ChoiceBox<String> dstStationChoiceBox;
  @FXML private Button confirmPathButton;
  @FXML private TableView<PathDetail> pathDetailTableView = new TableView<>();
  @FXML private TableView<TripSolution> tripSolutionsTableView = new TableView<>();
  @FXML private Button saveComputedPathButton;

  // Third Tab
  @FXML private TableView<Train> trainTableView;
  @FXML private TableView<Driver> driversTableView;
  @FXML private CheckBox isRvCheckBox;
  @FXML private Slider capacitySlider;
  @FXML private Button trainCreationButton;
  @FXML private ChoiceBox<String> driverChoiceBox;
  @FXML private Button refreshTrainTableViewButton;

  private DBGenerator dbGenerator;
  private PathController pathController;
  private SectionController sectionController;
  private TrainController trainController;
  private RouteInfoController routeInfoController;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.initializeSubControllers();
    this.fillTableViews();
    this.fillChoiceBoxes();
    this.initialButtonsSetup();
  }

  private void initializeSubControllers() {
    this.dbGenerator = new DBGenerator();
    this.pathController = new PathController(this.dbGenerator);
    this.sectionController = new SectionController(dbGenerator);
    this.trainController = new TrainController(dbGenerator);
    this.routeInfoController = new RouteInfoController(dbGenerator);
  }

  private void initialButtonsSetup() {
    this.confirmPathButton
        .disableProperty()
        .bind(
            this.srcStationChoiceBox
                .valueProperty()
                .isNull()
                .or(dstStationChoiceBox.valueProperty().isNull()));
    this.saveComputedPathButton
        .disableProperty()
        .bind(Bindings.size(this.sectionController.getPathDetails()).isEqualTo(0));

    this.capacitySlider
        .valueProperty()
        .addListener(
            new ChangeListener<Number>() {

              @Override
              public void changed(
                  ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                capacitySlider.setValue(newValue.intValue());
              }
            });

    this.trainCreationButton.disableProperty().bind(this.driverChoiceBox.valueProperty().isNull());
    this.routeConfirmationButton
        .disableProperty()
        .bind(
            this.datePicker.valueProperty().isNull()
            .or(this.pathChoiceBox.valueProperty().isNull())
            .or(this.trainChoiceBox.valueProperty().isNull())
        );
  }

  private void fillTableViews() {
    this.fillPathTableView();
    this.fillSectionTableView();
    this.fillTrainControllerView();
    this.fillResultTableView();
  }

  private void fillPathTableView() {
    this.genericTableFill(
        this.tripSolutionsTableView,
        this.pathController.getTableViewColumns(),
        this.pathController.getTripSolutions());
  }

  private void fillSectionTableView() {
    this.genericTableFill(
        this.pathDetailTableView,
        this.sectionController.getTableViewColumns(),
        this.sectionController.getPathDetails());
  }

  private void fillTrainControllerView() {
    this.genericTableFill(
        this.trainTableView,
        this.trainController.getTrainTableViewColumns(),
        this.trainController.getAllTrains());

    this.genericTableFill(
        this.driversTableView,
        this.trainController.getDriversTableViewColumns(),
        this.trainController.getAllDrivers());
  }

  private void fillResultTableView() {
    this.genericTableFill(
        this.resultTableView, 
        this.routeInfoController.getTableViewColumns(), 
        this.routeInfoController.getRouteInfos());
  }

  private <T> void genericTableFill(
      TableView<T> tableView, List<TableColumn<T, ?>> columns, ObservableList<T> elements) {
    tableView.setEditable(true);
    columns.forEach(t -> tableView.getColumns().add(t));
    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tableView.setItems(elements);
    tableView.refresh();
  }

  private void fillChoiceBoxes() {
    this.pathChoiceBox.getItems().setAll(this.pathController.getAllPathCodes());
    this.srcStationChoiceBox.getItems().setAll(this.pathController.getStations());
    this.dstStationChoiceBox.getItems().setAll(this.pathController.getStations());
    this.driverChoiceBox
        .getItems()
        .setAll(
            this.trainController.getAllDrivers().stream()
                .map(t -> t.getLicenceNumber())
                .collect(Collectors.toList()));

    this.trainChoiceBox
        .getItems()
        .setAll(
            this.trainController.getAllTrains().stream()
                .map(t -> t.getTrainCode())
                .collect(Collectors.toList()));
  }


  @FXML
  void saveRouteInfo(ActionEvent event) {
    final var date = this.getDateFromDatePicker();
    final var pathId = this.pathChoiceBox.getValue();
    final var trainId = this.trainChoiceBox.getValue();
    
    if (!this.routeInfoController.saveSelectedPathInfo(date, pathId, trainId)) {
      this.showDialog("Impossibile creare la percorrenza selezionata!");
    }
    this.clearRouteInfoButtons();
  }

  public Date getDateFromDatePicker() {
    return Utils
        .dateToSqlDate(
            Utils.buildDate(this.datePicker.getValue().getDayOfMonth(),
                this.datePicker.getValue().getMonthValue(),
                this.datePicker.getValue().getYear()).get());
  }

  private void clearRouteInfoButtons() {
    this.trainChoiceBox.setValue(null);
    this.pathChoiceBox.setValue(null);
    this.datePicker.setValue(null);
  }

  @FXML
  void refreshRouteInfo(ActionEvent event) {
    this.routeInfoController.refreshRouteInfos();
    this.resultTableView.refresh();
  }

  /**
   * Find the route from a to b, specifing all the paths (as in Controller.java)
   *
   * @param event
   */
  @FXML
  void getSelectedPathSections(ActionEvent event) {
    final var src = this.srcStationChoiceBox.getValue();
    final var dst = this.dstStationChoiceBox.getValue();

    if (src.equals(dst)) {
      showDialog("Le stazioni selezionate sono le medesime...");
      this.srcStationChoiceBox.valueProperty().set(null);
      this.dstStationChoiceBox.valueProperty().set(null);
      return;
    }

    this.sectionController.clearPathDetails();
    final var pathId =
        this.pathController
            .getPathFromStations(
                this.srcStationChoiceBox.getValue(), this.dstStationChoiceBox.getValue())
            .get();

    this.sectionController.computeSectionsFromPath(pathId);
    this.pathDetailTableView.refresh();
    this.srcStationChoiceBox.valueProperty().set(null);
    this.dstStationChoiceBox.valueProperty().set(null);
  }

  // save computed route in the getSelectedPath to the database.
  @FXML
  void saveComputedPathToDb(ActionEvent event) {}

  @FXML
  void saveTrainToDb(ActionEvent event) {
    if (!this.trainController.addNewTrain(
        this.driverChoiceBox.getValue(),
        this.isRvCheckBox.isSelected(),
        (int) this.capacitySlider.getValue())) {
      showDialog("Cannot Insert this train!");
      return;
    }

    this.driverChoiceBox.setValue(null);
    this.trainTableView.refresh();
  }

  @FXML
  void refreshTrainTableView(ActionEvent event) {
    this.trainController.refreshTrains();
    this.trainTableView.refresh();
    this.trainChoiceBox.getItems().clear();
    this.trainChoiceBox
        .getItems()
        .setAll(
            this.trainController.getAllTrains().stream()
                .map(t -> t.getTrainCode())
                .collect(Collectors.toList()));
  }

  private void showDialog(String msg) {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle("Route Distance");
    dialog.show();
    dialog.setContentText(msg);
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
    dialog.setHeight(dialog.getHeight() + 30);
  }
}
