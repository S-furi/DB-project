package db_project.view.controller;

import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import db_project.db.dbGenerator.DBGenerator;
import db_project.db.tables.CarClassTable;
import db_project.db.tables.CarTable;
import db_project.db.tables.DriverTable;
import db_project.db.tables.SeatTable;
import db_project.db.tables.TrainTable;
import db_project.model.Driver;
import db_project.model.Train;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class TrainController {
  private final DBGenerator dbGenerator;
  private TrainTable trainTable;
  private DriverTable driverTable;
  private CarClassTable carClassTable;
  private CarTable carTable;
  private SeatTable seatTable;
  private final Logger logger;

  private ObservableList<Train> trains;
  private ObservableList<Driver> drivers;
  private int lastTrainId;

  public TrainController(final DBGenerator dbGenerator) {
    this.dbGenerator = dbGenerator;
    this.getTables();
    this.trains = FXCollections.observableArrayList();
    this.drivers = FXCollections.observableArrayList();
    this.lastTrainId = this.getLastTrainId();
    this.logger = Logger.getLogger("TrainController");
    this.logger.setLevel(Level.WARNING);
  }

  private void getTables() {
    this.trainTable = (TrainTable) dbGenerator.getTableByClass(TrainTable.class);
    this.driverTable = (DriverTable) dbGenerator.getTableByClass(DriverTable.class);
    this.carClassTable = (CarClassTable) dbGenerator.getTableByClass(CarClassTable.class);
    this.carTable = (CarTable) dbGenerator.getTableByClass(CarTable.class);
    this.seatTable = (SeatTable) dbGenerator.getTableByClass(SeatTable.class);
  }

  public boolean addNewTrain(final String licenseNumber, final boolean isRv, final int capacity) {
    final Train train = new Train(this.getNewTrainId(), licenseNumber, capacity, isRv);
    this.logger.info(train.toString());
    return this.trainTable.save(train);
  }

  private String getNewTrainId() {
    return String.valueOf(++this.lastTrainId);
  }

  private int getLastTrainId() {
    final var lastTrain =
        this.trainTable.findAll().stream()
            .sorted(
                (t1, t2) -> // reversed sort
                Integer.compare(
                        Integer.parseInt(t2.getLicenseNumber()),
                        Integer.parseInt(t1.getLicenseNumber())))
            .findFirst();
    if (lastTrain.isPresent()) {
      return Integer.valueOf(lastTrain.get().getTrainCode()) + 1;
    }
    return 0;
  }

  public List<TableColumn<Train, ?>> getTrainTableViewColumns() {
    TableColumn<Train, String> trainCodeColumn = new TableColumn<>("Codice Treno");
    trainCodeColumn.setCellValueFactory(new PropertyValueFactory<>("trainCode"));
    TableColumn<Train, String> licenceNumberColumn = new TableColumn<>("Codice Macchinista");
    licenceNumberColumn.setCellValueFactory(new PropertyValueFactory<>("licenseNumber"));
    TableColumn<Train, Integer> capacityColumn = new TableColumn<>("Capienza");
    capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));
    TableColumn<Train, String> isRvColumn = new TableColumn<>("Regionale Veloce");
    isRvColumn.setCellValueFactory(new PropertyValueFactory<>("isRv"));

    return List.of(trainCodeColumn, licenceNumberColumn, capacityColumn, isRvColumn);
  }

  public List<TableColumn<Driver, ?>> getDriversTableViewColumns() {
    TableColumn<Driver, String> licenceNumberColumn = new TableColumn<>("Codice Macchinista");
    licenceNumberColumn.setCellValueFactory(new PropertyValueFactory<>("licenceNumber"));
    TableColumn<Driver, Date> contractYearColumn = new TableColumn<>("Anno Contratto");
    contractYearColumn.setCellValueFactory(new PropertyValueFactory<>("contractYear"));
    TableColumn<Driver, String> firstNameColumn = new TableColumn<>("Nome");
    firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    TableColumn<Driver, String> lastNameColumn = new TableColumn<>("Cognome");
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    TableColumn<Driver, String> telephoneColumn = new TableColumn<>("Telefono");
    telephoneColumn.setCellValueFactory(new PropertyValueFactory<>("telephone"));
    TableColumn<Driver, String> emailColumn = new TableColumn<>("Email");
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    TableColumn<Driver, String> residenceColumn = new TableColumn<>("Residenza");
    residenceColumn.setCellValueFactory(new PropertyValueFactory<>("residence"));

    return List.of(
        licenceNumberColumn,
        contractYearColumn,
        firstNameColumn,
        lastNameColumn,
        telephoneColumn,
        emailColumn,
        residenceColumn);
  }

  public ObservableList<Train> getAllTrains() {
    this.refreshTrains();
    return this.trains;
  }

  public ObservableList<Driver> getAllDrivers() {
    this.drivers.clear();
    this.drivers.addAll(
        this.driverTable.findAll().stream()
            .sorted(
                (d1, d2) ->
                    Integer.compare(
                        Integer.parseInt(d1.getLicenceNumber()),
                        Integer.parseInt(d2.getLicenceNumber())))
            .collect(Collectors.toList()));

    return this.drivers;
  }

  public void refreshTrains() {
    this.trains.clear();
    this.trains.addAll(
        this.trainTable.findAll().stream()
            .sorted(
                (t1, t2) ->
                    Integer.compare(
                        Integer.parseInt(t1.getTrainCode()), Integer.parseInt(t2.getTrainCode())))
            .collect(Collectors.toList()));
  }
}
