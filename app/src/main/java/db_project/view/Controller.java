package db_project.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import db_project.db.ConnectionProvider;
import db_project.db.tables.UserTable;
import db_project.model.User;
import db_project.utils.SampleLoader;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller implements Initializable{
    @FXML
    private Button erase_button;

    @FXML
    private Button register_button;

    @FXML
    private Button columnRemover;
    
    @FXML
    private TextField email_field = new TextField();

    @FXML
    private TextField firstname_field = new TextField();

    @FXML
    private TextField lastname_field = new TextField();

    @FXML
    private TextField tel_field = new TextField();

    @FXML
    private TableView<User> table = new TableView<>();

    private ObservableList<User> users = FXCollections.observableArrayList();

    final static ConnectionProvider connectionProvider =  ConnectionController.getConnectionProvider();

    final static UserTable users_table = new UserTable(connectionProvider.getMySQLConnection());
    
    final SampleLoader sample = new SampleLoader("data_generation/users/user_data/db_data.json");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.createTableView();

        register_button.disableProperty().bind(
            Bindings.isEmpty(firstname_field.textProperty())
            .or(Bindings.isEmpty(lastname_field.textProperty()))
            .or(Bindings.isEmpty(tel_field.textProperty()))
            .or(Bindings.isEmpty(email_field.textProperty()))
        );

        sample.getStoredPeople().forEach(t -> this.addUserToCurrentTable(t));

        System.out.println("Setup completed: " + this.resetSqlTable());
    }

    /**
     * Resetting mySql table
     * @return false if the operation could not be completed
     */
    private boolean resetSqlTable() {
        return  users_table.dropTable() && users_table.createTable();
    }

    /**
     * Creates table view with 4 coulumns. 
     */
    public void createTableView() {
        this.table.setEditable(true);

        TableColumn<User, String> firstNameColumn = new TableColumn<>("First Name");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));

        TableColumn<User, String> lastNameColumn = new TableColumn<>("Last Name");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));

        TableColumn<User, String> telColumn = new TableColumn<>("Tel");
        telColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));

        TableColumn<User, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        var columns = List.of(firstNameColumn, lastNameColumn, telColumn, emailColumn);
        columns.forEach(t -> this.table.getColumns().add(t));

        this.table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.table.setItems(this.users);
    }
    
    
    @FXML
    void eraseAllFields(ActionEvent event) {
        List.of(this.email_field,
            this.firstname_field,
            this.lastname_field,
            this.tel_field).forEach(t -> t.clear());;
    }

    @FXML
    void saveRegistration(ActionEvent event) {
        var id = User.generateId();
        
        User user = new User(id, this.firstname_field.getText(), 
                                 this.lastname_field.getText(),
                                 this.tel_field.getText(),
                                 this.email_field.getText());
        
        System.out.println("Registration completed");
        System.out.println("New user's info: " + user.getUserInfo());
        this.eraseAllFields(event);
        this.addUserToCurrentTable(user);
    }


    private void addUserToCurrentTable(User user) {
        this.users.add(user);
        users_table.save(user);
        this.table.refresh();
    }

    @FXML
    void removeColumn(ActionEvent event) {
        this.users.remove(this.users.get(0));
    }
}
