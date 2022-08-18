package db_project.view.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import db_project.db.dbGenerator.DBGenerator;
import db_project.db.tables.CarClassTable;
import db_project.db.tables.CarTable;
import db_project.db.tables.DriverTable;
import db_project.db.tables.SeatTable;
import db_project.db.tables.TrainTable;
import db_project.model.Car;
// import db_project.model.CarClass;
import db_project.model.Driver;
import db_project.model.Seat;
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
  private int lastCarNumber;

  public TrainController(final DBGenerator dbGenerator) {
    this.dbGenerator = dbGenerator;
    this.getTables();
    this.trains = FXCollections.observableArrayList();
    this.drivers = FXCollections.observableArrayList();
    this.lastCarNumber = 1;
    this.logger = Logger.getLogger("TrainController");
    this.logger.setLevel(Level.WARNING);
    this.lastTrainId = this.getLastTrainId();
    logger.info("Last train inserted is number: " + (this.lastTrainId - 1));
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
    return this.trainTable.save(train)
        && this.saveCarAndSeatsDetails(train.getTrainCode(), train.getCapacity());
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
                        Integer.parseInt(t2.getTrainCode()), Integer.parseInt(t1.getTrainCode())))
            .findFirst();
    if (lastTrain.isPresent()) {
      return Integer.valueOf(lastTrain.get().getTrainCode()) + 1;
    }
    return 0;
  }

  public boolean saveCarAndSeatsDetails(final String trainId, final int capacity) {
    final List<Car> cars = this.generateCarsFromTrain(trainId, capacity);
    final List<Seat> seats = new ArrayList<>();
    cars.forEach(t -> seats.addAll(this.getSeats(t)));

    return this.saveCarsToDb(cars) && this.saveSeatsToDb(seats);
  }

  private List<Car> generateCarsFromTrain(final String trainId, final int capacity) {
    final var carClasses = this.carClassTable.findAll();

    final List<Car> cars = new ArrayList<>();
    carClasses.forEach(
        t -> cars.addAll(this.createCarDetails(trainId, t.getClassType(), capacity)));
    return cars;
  }

  // private void updateClassesSeats(final List<Car> cars) {
  //   this.carClassTable
  //       .findAll()
  //       .forEach(t -> {
  //         this.carClassTable.update(
  //           new CarClass(t.getClassType(), this.getTotalSeatsByClass(t.getClassType(), cars))
  //         );
  //       });
  // }

  // private int getTotalSeatsByClass(final String classType, List<Car> cars) {
  //   return cars
  //       .stream()
  //       .filter(t -> t.getClassType().equals(classType))
  //       .map(t -> t.getSeats())
  //       .collect(Collectors.summingInt(Integer::intValue));
  // }

  private List<Car> createCarDetails(
      final String trainId, final String classType, final int capacity) {
    int firstClassSeats = 0;
    if ((capacity / 100) % 2 == 0) {
      firstClassSeats = capacity / 2 - 100;
    } else {
      firstClassSeats = capacity / 2 - 150;
    }

    if (classType.equals("1")) {
      return this.getCars(trainId, classType, firstClassSeats / Car.NUMBER_FIRST_CLASS_SEATS);
    } else {
      final int secondClassSeats = capacity - firstClassSeats;
      return this.getCars(trainId, classType, secondClassSeats / Car.NUMBER_SECOND_CLASS_SEATS);
    }
  }

  private List<Car> getCars(final String trainId, final String classType, final int numOfCars) {
    final Random rand = new Random();
    final List<Car> cars = new ArrayList<>();

    for (int i = 0; i < numOfCars; i++) {
      var car =
          new Car(
              classType,
              trainId,
              this.lastCarNumber++,
              classType.equals("1") ? Car.NUMBER_FIRST_CLASS_SEATS : Car.NUMBER_SECOND_CLASS_SEATS,
              rand.nextBoolean());
      cars.add(car);
    }
    return cars;
  }

  private List<Seat> getSeats(final Car car) {
    final List<Seat> seats = new ArrayList<>();
    final int numOfSeats =
        car.getClassType().equals("1")
            ? Car.NUMBER_FIRST_CLASS_SEATS
            : Car.NUMBER_SECOND_CLASS_SEATS;

    for (int i = 0; i < numOfSeats; i++) {
      var seat = new Seat(car, i + 1);
      seats.add(seat);
    }
    return seats;
  }

  private boolean saveSeatsToDb(List<Seat> seats) {
    for (final var seat : seats) {
      if (!this.seatTable.save(seat)) {
        return false;
      }
    }
    return true;
  }

  private boolean saveCarsToDb(List<Car> cars) {
    for (final var car : cars) {
      if (!this.carTable.save(car)) {
        return false;
      }
    }
    return true;
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

    final List<TableColumn<Train, ?>> lst =
        List.of(trainCodeColumn, licenceNumberColumn, capacityColumn, isRvColumn);

    lst.forEach(t -> t.setStyle("-fx-alignment: CENTER;"));

    return lst;
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

    final List<TableColumn<Driver, ?>> lst =
        List.of(
            licenceNumberColumn,
            contractYearColumn,
            firstNameColumn,
            lastNameColumn,
            telephoneColumn,
            emailColumn,
            residenceColumn);

    lst.forEach(t -> t.setStyle("-fx-alignment: CENTER;"));

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
