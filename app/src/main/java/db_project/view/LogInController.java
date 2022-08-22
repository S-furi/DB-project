package db_project.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import db_project.utils.Authenticator;
import db_project.utils.authentication.AuthResponses;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class LogInController implements Initializable {

  @FXML private TextField usernameField;

  @FXML private PasswordField passField;

  @FXML private Button logInButton;

  @FXML private CheckBox cartArrowReg;

  @FXML
  void validateData(ActionEvent event) {
    var email = this.usernameField.getText();
    var password = this.passField.getText();
    AuthResponses response = Authenticator.authenticate(email, password);
    System.out.println(response);
    switch (response) {
      case USER:
        this.switchToUserLanding(event, email);
        break;
      case ROOT:
        this.switchToRootLanding(event);
        break;
      case DENIED:
        this.showDialog("USERNAME O PASSWORD NON CORRETTI");
        break;
      default:
        break;
    }
  }

  @FXML
  void switchToRegister(ActionEvent event) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/NormalUserRegistration.fxml"));
      var stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      var scene = new Scene(root);
      stage.setScene(scene);
      stage.show();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void switchToUserLanding(ActionEvent event, String usrEmail) {
    try{
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/TestTicketReservation.fxml"));
      final TestTicketBuy ticketBuyController = new TestTicketBuy();
      ticketBuyController.setUsrEmail(usrEmail);
      loader.setController(ticketBuyController);
      Parent root = (Parent) loader.load();
      var stage = (Stage)((Node) event.getSource()).getScene().getWindow();
      var scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  private void switchToRootLanding(ActionEvent event) {
    try {
      Parent root = FXMLLoader.load(getClass().getResource("/TripSolutions.fxml"));
      var stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      var scene = new Scene(root);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  private void showDialog(String msg) {
    Dialog<String> dialog = new Dialog<>();
    dialog.setTitle("DATI NON CORRETTI");
    dialog.show();
    dialog.setContentText(msg);
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
    dialog.setHeight(dialog.getHeight() + 30);
  }
}
