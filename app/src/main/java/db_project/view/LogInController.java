package db_project.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class LogInController implements Initializable {

    @FXML
    private TextField mailEntry;

    @FXML
    private Button registerButton;

    @FXML
    private ChoiceBox<?> cityLabel;

    @FXML
    private ChoiceBox<?> streetLabel;

    @FXML
    private AnchorPane pannelloLog;

    @FXML
    private ChoiceBox<?> provLabel;

    @FXML
    private ChoiceBox<?> regLabel;

    @FXML
    private Button logInButton;

    @FXML
    private AnchorPane pannelloReg;

    @FXML
    private TextField passwordEntry;
    @FXML
    void logIn(ActionEvent event) {
        System.out.println(this.mailEntry.getText());
    }

    @FXML
    void registerUser(ActionEvent event) {
        this.pannelloLog.setVisible(false);
        this.pannelloReg.setVisible(true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.pannelloReg.setVisible(false);
        this.pannelloLog.setVisible(true);
    }

}
