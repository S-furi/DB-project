package db_project.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class RegistrationController implements Initializable {

    @FXML
    private Button regUser;

    @FXML
    private ChoiceBox<String>  cittReg;

    @FXML
    private TextField surnameReg;

    @FXML
    private TextField emailReg;

    @FXML
    private ChoiceBox<String>  regReg;

    @FXML
    private TextField phoneNumReg;

    @FXML
    private ChoiceBox<String> accountTypes;

    @FXML
    private TextField nameReg;

    @FXML
    private ChoiceBox<String>  provReg;

    @FXML
    private TextField licId;

    @FXML
    private CheckBox cartArrowReg;

    private List<String> accounts;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.populateAccountTypes();

        this.accountTypes.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> ov, Number old_val, Number new_val )-> {
            if(this.accounts.get(new_val.intValue()) == "Macchinista"){
                this.licId.setVisible(true);
            }else{
                this.licId.setVisible(false);
            }
            if(this.accounts.get(new_val.intValue()) != "Utente"){
                this.cartArrowReg.setVisible(false);
            }else{
                this.cartArrowReg.setVisible(true);
            }
        });
    }
    
    private void populateAccountTypes(){
        this.accounts = new ArrayList<>();
        this.accounts.addAll(List.of("Utente", "Amministratore", "Responsabile Stazione", "Macchinista" ));
        this.accountTypes.getItems().setAll(this.accounts);
        this.accountTypes.setValue("Utente");
    }




}
