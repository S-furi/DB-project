package db_project.view;

import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import db_project.db.dbGenerator.DBGenerator;
import db_project.utils.Utils;
import db_project.view.adminPanelController.RouteInfoController;
import db_project.view.adminPanelController.TrainController;
import db_project.view.adminPanelController.RouteInfoController.DateTripSolution;
import db_project.view.userPanelController.TicketBuyController;
import db_project.view.userPanelController.TicketBuyController.TicketCheckout;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class TestTicketBuy implements Initializable {
  @FXML private Button buyTicketButtton;
  @FXML private CheckBox firstClassCheckBox;
  @FXML private CheckBox isRVCheckBox;
  @FXML private ChoiceBox<String> routeInfoTrainIdChoiceBox;
  @FXML private ChoiceBox<String> routeInfoPathIdChoiceBox;
  @FXML private TableView<DateTripSolution> routeInfoTableView;
  @FXML private TableView<TicketCheckout> ticketDetailTableView;
  @FXML private DatePicker routeInfoDatePicker;

  private DBGenerator dbGenerator;
  private TicketBuyController ticketController;
  private RouteInfoController routeInfoController;
  private TrainController trainController;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.initializeDb();
    this.initializeSubControllers();
    this.fillTableViews();
    this.fillChoiceBoxes();
    this.disableButtons();
  }

  private void initializeDb() {
    this.dbGenerator = new DBGenerator();
    this.dbGenerator.createDB();
    this.dbGenerator.createTables();
  }

  private void initializeSubControllers() {
    this.ticketController = new TicketBuyController(dbGenerator);
    this.routeInfoController = new RouteInfoController(dbGenerator);
    this.trainController = new TrainController(dbGenerator);
  }

  private void fillTableViews() {
    this.fillTicketBuyController();
  }

  private void disableButtons() {
    this.buyTicketButtton
        .disableProperty()
        .bind(
            this.routeInfoTrainIdChoiceBox
                .valueProperty()
                .isNull()
                .or(this.routeInfoPathIdChoiceBox.valueProperty().isNull())
                .or(this.routeInfoDatePicker.valueProperty().isNull()));

    this.routeInfoTrainIdChoiceBox
        .disableProperty()
        .bind(this.routeInfoPathIdChoiceBox.valueProperty().isNull());
  }

  private void fillChoiceBoxes() {
    this.routeInfoTrainIdChoiceBox
        .getItems()
        .setAll(
            this.trainController.getAllTrains().stream()
                .map(t -> t.getTrainCode())
                .collect(Collectors.toList()));
    this.routeInfoPathIdChoiceBox
        .getItems()
        .setAll(
            this.routeInfoController.getRouteInfos().stream()
                .map(t -> t.getPathId())
                .collect(Collectors.toList()));
  }

  private void fillTicketBuyController() {
    this.genericTableFill(
        this.routeInfoTableView,
        this.routeInfoController.getTableViewColumns(),
        this.routeInfoController.getRouteInfos());
    this.genericTableFill(
        this.ticketDetailTableView,
        this.ticketController.getTableViewColumns(),
        this.ticketController.getBoughtTicketDetails());
  }

  private <T> void genericTableFill(
      TableView<T> tableView, List<TableColumn<T, ?>> columns, ObservableList<T> elements) {
    tableView.setEditable(true);
    columns.forEach(t -> tableView.getColumns().add(t));
    tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    tableView.setItems(elements);
    tableView.refresh();
  }

  @FXML
  void registerTicketBought(ActionEvent event) {
    final Date date = this.getDateFromDatePicker();
    final String pathId = this.routeInfoPathIdChoiceBox.getValue();
    final String trainId = this.routeInfoTrainIdChoiceBox.getValue();
    final boolean isRv = this.isRVCheckBox.isSelected();
    final boolean isFirstClass = this.firstClassCheckBox.isSelected();

    if (!this.ticketController.registerTicketBought(date, pathId, trainId, isRv, isFirstClass)) {
      this.showDialog("Impossibile acquistare il biglietto, reinserire i dati e riprovare...");
    }
    this.restoreFxElements();
  }

  private void restoreFxElements() {
    this.routeInfoDatePicker.setValue(null);
    this.routeInfoPathIdChoiceBox.setValue(null);
    this.routeInfoTrainIdChoiceBox.setValue(null);
    this.isRVCheckBox.setSelected(false);
    this.firstClassCheckBox.setSelected(false);
  }

  private Date getDateFromDatePicker() {
    return Utils.dateToSqlDate(
        Utils.buildDate(
                this.routeInfoDatePicker.getValue().getDayOfMonth(),
                this.routeInfoDatePicker.getValue().getMonthValue(),
                this.routeInfoDatePicker.getValue().getYear())
            .get());
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
