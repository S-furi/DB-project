package db_project.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db_project.db.ConnectionProvider;
import db_project.db.tables.CityTable;
import db_project.db.tables.PassengerTable;
import db_project.model.Passenger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Node;

public class RegistrationController implements Initializable {

  @FXML private ChoiceBox<String> cittReg;

  @FXML private TextField licId;

  @FXML private Button submitButton;

  @FXML private TextField surnameReg;

  @FXML private AnchorPane alert;

  @FXML private TextField emailReg;

  @FXML private ChoiceBox<String> regReg;

  @FXML private TextField phoneNumReg;

  @FXML private TextField nameReg;

  @FXML private CheckBox cartArrowReg;

  @FXML private ChoiceBox<String> provReg;

  private List<String> data;

  private static String username = "root";
  private static String password = "123Test123";
  private static String db_name = "Railway";

  private static final ConnectionProvider connectionProvider =
      new ConnectionProvider(username, password, db_name);
  private static final CityTable cityTable = new CityTable(connectionProvider.getMySQLConnection());
  private static final PassengerTable passengerTable =
      new PassengerTable(connectionProvider.getMySQLConnection());
  // private static final AdminTable adminTable = new
  // AdminTable(connectionProvider.getMySQLConnection());

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    this.populateCountryData();

    this.populateBoxes();
  }

  private void populateCountryData() {
    this.regReg.setValue("Regione");
    this.provReg.setValue("Provincia");
    this.cittReg.setValue("Citta'");
  }

  @FXML
  private void retrieveData(ActionEvent event) {
    this.data = new ArrayList<>();

    this.data.add(nameReg.getText());
    this.data.add(surnameReg.getText());
    this.data.add(phoneNumReg.getText());
    this.data.add(emailReg.getText());
    this.data.add(regReg.getValue());
    this.data.add(provReg.getValue());
    this.data.add(cittReg.getValue());

    if (this.cartArrowReg.isSelected()) {
      this.data.add("true");
    } else {
      this.data.add("false");
    }

    if (!this.validateData()) {
      System.out.println("DATI NON CORRETTI");
      this.nameReg.clear();
      this.surnameReg.clear();
      this.phoneNumReg.clear();
      this.emailReg.clear();
    }
    this.executeData(data, event);
  }

  private void executeData(List<String> data, ActionEvent event) {
    var newID =
        passengerTable.findAll().stream()
            .map(t -> t.getPassengerCode())
            .sorted((t1, t2) -> Integer.compare(Integer.parseInt(t2), Integer.parseInt(t1)))
            .findFirst()
            .get();
    var newUser =
        new Passenger(
            Integer.toString(Integer.parseInt(newID) + 1),
            data.get(0),
            data.get(1),
            data.get(2),
            data.get(3),
            data.get(6));
    passengerTable.save(newUser);
    this.switchToLogIn(event);
  }

  private boolean validateData() {
    boolean allNotEmpty = this.data.stream().allMatch(x -> x.isEmpty() == false);
    return allNotEmpty;
  }

  private void populateBoxes() {
    cityTable
        .findAll()
        .forEach(
            t -> {
              this.regReg.getItems().add(t.getRegion());
              this.provReg.getItems().add(t.getProvince());
              this.cittReg.getItems().add(t.getName());
            });
  }

  private void switchToLogIn(ActionEvent event) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
      var stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      var scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
