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
import db_project.view.adminPanelController.PathController;
import db_project.view.adminPanelController.RouteInfoController;
import db_project.view.adminPanelController.SectionController;
import db_project.view.adminPanelController.SubsController;
import db_project.view.adminPanelController.TrainController;
import db_project.view.adminPanelController.PathController.TripSolution;
import db_project.view.adminPanelController.RouteInfoController.DateTripSolution;
import db_project.view.adminPanelController.SectionController.PathDetail;
import db_project.view.adminPanelController.SubsController.Subscriber;
import db_project.view.userPanelController.TicketBuyController;
import db_project.view.userPanelController.TicketBuyController.TicketCheckout;
import javafx.fxml.Initializable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
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
  @FXML private Button refreshPathTableButton;

  // Third Tab
  @FXML private TableView<Train> trainTableView;
  @FXML private TableView<Driver> driversTableView;
  @FXML private CheckBox isRvCheckBox;
  @FXML private Slider capacitySlider;
  @FXML private Button trainCreationButton;
  @FXML private ChoiceBox<String> driverChoiceBox;
  @FXML private Button refreshTrainTableViewButton;

  // Fourth Tab
  @FXML private ChoiceBox<String> subscribersChoiceBox;
  @FXML private TableView<Subscriber> subscribersTableView;
  @FXML private Button showAllSubscribersButton;
  @FXML private Button findSubscriberButton;

  // Fifth Tab
  @FXML private TableView<TicketCheckout> ticketsTableView;
  @FXML private BarChart<String, Integer> monthBarChart;
  @FXML private CategoryAxis monthsAxis;
  @FXML private NumberAxis ticketsAxis;

  private DBGenerator dbGenerator;
  private PathController pathController;
  private SectionController sectionController;
  private TrainController trainController;
  private RouteInfoController routeInfoController;
  private SubsController subsController;
  private TicketBuyController ticketController;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.initializeSubControllers();
    this.fillTableViews();
    this.fillChoiceBoxes();
    this.initialButtonsSetup();
    this.fillTicketsBarChart();
  }

  private void initializeSubControllers() {
    this.dbGenerator = new DBGenerator();
    this.pathController = new PathController(this.dbGenerator);
    this.sectionController = new SectionController(dbGenerator, this.pathController);
    this.trainController = new TrainController(dbGenerator);
    this.routeInfoController = new RouteInfoController(dbGenerator, this.sectionController);
    this.subsController = new SubsController(dbGenerator);
    this.ticketController = new TicketBuyController(dbGenerator);
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
            this.datePicker
                .valueProperty()
                .isNull()
                .or(this.pathChoiceBox.valueProperty().isNull())
                .or(this.trainChoiceBox.valueProperty().isNull()));

    this.findSubscriberButton
        .disableProperty()
        .bind(this.subscribersChoiceBox.valueProperty().isNull());
  }

  private void fillTableViews() {
    this.fillPathTableView();
    this.fillSectionTableView();
    this.fillTrainControllerView();
    this.fillResultTableView();
    this.fillSubscribersTableView();
    this.fillTicketsTableView();
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

  private void fillSubscribersTableView() {
    this.genericTableFill(
        this.subscribersTableView,
        this.subsController.getSubscribersTableViewColumns(),
        this.subsController.getAllSubscribers());
  }

  private void fillTicketsTableView() {
    this.genericTableFill(
        this.ticketsTableView,
        this.ticketController.getTableViewColumns(),
        this.ticketController.getAllBoughtTicket());
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
    this.subscribersChoiceBox
        .getItems()
        .setAll(
            this.subsController.getSubscribers().stream()
                .map(t -> t.getPassengerCode())
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
    return Utils.dateToSqlDate(
        Utils.buildDate(
                this.datePicker.getValue().getDayOfMonth(),
                this.datePicker.getValue().getMonthValue(),
                this.datePicker.getValue().getYear())
            .get());
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
  void showSelectedPathSections(ActionEvent event) {
    // admin autoAssegnato
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
        this.pathController.getPathCodeFromStationNames(
            this.srcStationChoiceBox.getValue(), this.dstStationChoiceBox.getValue());

    if (!this.sectionController.findSolution(pathId)) {
      showDialog("Le stazioni selezionate sono le medesime...");
      this.srcStationChoiceBox.valueProperty().set(null);
      this.dstStationChoiceBox.valueProperty().set(null);
      return;
    }

    // this.sectionController.computeSectionsFromPath(pathId);
    this.pathDetailTableView.refresh();
    this.srcStationChoiceBox.valueProperty().set(null);
    this.dstStationChoiceBox.valueProperty().set(null);
  }

  // save computed route in the getSelectedPath to the database.
  @FXML
  void saveComputedPathToDb(ActionEvent event) {
    if (!this.pathController.savePathToDb(this.sectionController.getComputedPath())
        || !this.sectionController.savePathInfosToDb()) {
      this.showDialog("Si è verificato un errore durante il salvataggio, riprovare.");
      this.pathController.delete(this.sectionController.getComputedPath());
      this.sectionController.clearPathDetails();
      return;
    }
    this.sectionController.clearPathDetails();
  }

  @FXML
  void refreshPathTableView(ActionEvent event) {
    this.pathController.refreshSolutions();
    this.tripSolutionsTableView.refresh();
  }

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

  @FXML
  void showAllSubscribers(ActionEvent event) {
    if (!this.subsController.findAllSubscribers()) {
      this.showDialog("There aren't subscribers inside DB!");
    }
  }

  @FXML
  void findSubscriber(ActionEvent event) {
    final var subscriber = this.subscribersChoiceBox.getValue();
    this.subscribersChoiceBox.setValue(null);
    if (!this.subsController.findSubscriber(subscriber)) {
      this.showDialog("Cannot find Selected subscriber!");
      return;
    }
  }

  private void fillTicketsBarChart() {
    XYChart.Series<String, Integer> dataSet = new XYChart.Series<>();
    for (int i = 1; i <= 12; i++) {
      dataSet
          .getData()
          .add(
              new XYChart.Data<String, Integer>(
                  String.valueOf(i), this.ticketController.getSoldTicketByMonth(i)));
    }
    this.monthBarChart.getData().add(dataSet);
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
