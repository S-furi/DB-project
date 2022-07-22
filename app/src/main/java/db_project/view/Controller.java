package db_project.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import db_project.db.ConnectionProvider;
import db_project.db.tables.UserTable;
import db_project.model.User;
import db_project.utils.SampleLoader;
import db_project.view.viewUtils.StationWithCheckBox;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Controller implements Initializable{
    private final static ConnectionProvider connectionProvider =  ConnectionController.getConnectionProvider();
    private final SampleLoader sample = new SampleLoader("data_generation/users/user_data/db_data.json");

    /* Users */
    @FXML
    private Button eraseButton;
    @FXML
    private Button registerButton = new Button();
    @FXML
    private Button columnRemover;
    @FXML
    private Button searchSolutionsButton;
    @FXML
    private TextField emailField = new TextField();
    @FXML
    private TextField firstnameField = new TextField();
    @FXML
    private TextField lastnameField = new TextField();
    @FXML
    private TextField telField = new TextField();
    @FXML
    private TableView<User> table = new TableView<>();
    private ObservableList<User> users = FXCollections.observableArrayList();
    private final static UserTable users_table = new UserTable(connectionProvider.getMySQLConnection());
    
    /* Railway */
    @FXML
    private TableView<StationWithCheckBox> solutionsTable;
    @FXML
    private Button saveSolutionsButton;
    @FXML
    private ChoiceBox<String> dstChoiceBox = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> srcChoiceBox = new ChoiceBox<>();

    private final RailwayController railwayController = new RailwayController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.createUsersTableView();
        this.createStationsTableView();
        this.resetSqlTable();

        this.initialButtonsSetup();

        sample.getStoredPeople().forEach(t -> this.addUserToCurrentTable(t));

        this.populateCheckBox();
        
        System.out.println("Setup completed: " + this.resetSqlTable());
    }

    /**
     * Resetting mySql table
     * @return false if the operation could not be completed
     */
    private boolean resetSqlTable() {
        return  users_table.dropTable() && users_table.createTable();
    }

    public void initialButtonsSetup() {
        this.registerButton.disableProperty().bind(
            Bindings.isEmpty(firstnameField.textProperty())
            .or(Bindings.isEmpty(lastnameField.textProperty()))
            .or(Bindings.isEmpty(telField.textProperty()))
            .or(Bindings.isEmpty(emailField.textProperty()))
        );

        this.searchSolutionsButton.disableProperty().bind(
            this.srcChoiceBox.valueProperty().isNull()
            .or(dstChoiceBox.valueProperty().isNull())
        );

        this.saveSolutionsButton.setDisable(true);
    }

    private void populateCheckBox() {
        this.srcChoiceBox.getItems().setAll(this.railwayController.getStationsNames());
        this.dstChoiceBox.getItems().setAll(this.railwayController.getStationsNames());
    }

    /**
     * Creates table view with 4 coulumns. 
     */
    public void createUsersTableView() {
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
        List.of(this.emailField,
            this.firstnameField,
            this.lastnameField,
            this.telField).forEach(t -> t.clear());;
    }

    @FXML
    void saveRegistration(ActionEvent event) {
        var id = User.generateId();
        
        User user = new User(id, this.firstnameField.getText(), 
                                 this.lastnameField.getText(),
                                 this.telField.getText(),
                                 this.emailField.getText());
        
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
    void removeLastRow(ActionEvent event) {
        this.users.remove(this.users.get(0));
    }

    private void createStationsTableView() {
        this.solutionsTable.setEditable(true);
        this.railwayController
            .getTableViewColumns()
            .forEach(t -> this.solutionsTable.getColumns().add(t));

        this.solutionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.solutionsTable.setItems(this.railwayController.getStations());
    }

    /**
     * Compute route from dst to src choosen from dropdown boxes.
     * 
     * @param event
     */
    @FXML
    void saveDstSrcInfo(ActionEvent event) {
        final var srcStation = this.srcChoiceBox.getValue();
        final var dstStation = this.dstChoiceBox.getValue();
        if (!(srcStation == null) && !(dstStation == null)) {
            System.out.println(srcStation + " to " + dstStation + " has been selected");
        }
        this.updateSolutionsTable(srcStation, dstStation);
        this.saveSolutionsButton.setDisable(false);
    }

    /**
     * Saves and display current path from source to destination.
     * 
     * @param srcStation
     * @param dstStation
     */
    private void updateSolutionsTable(String srcStation, String dstStation) {
        List<String> path = this.railwayController.computePath(srcStation, dstStation);
        path.forEach(t -> this.railwayController.addStation(new StationWithCheckBox(t)));
        this.solutionsTable.refresh();
    }

    /**
     * Once the user has selected wich stations to include 
     * in the route, this method clears the table and the 
     * dropdown boxes and saves the result.
     * 
     * @param event pressing confirm button
     */
    @FXML
    void computeRoute(ActionEvent event) {
        final var selectedStations = this.solutionsTable
            .getItems()
            .stream()
            .filter(t -> t.getSelect().isSelected())
            .map(t -> t.getName())
            .collect(Collectors.toList());
        selectedStations.forEach(System.out::println);
        this.solutionsTable.getItems().clear();
        this.srcChoiceBox.setValue(null);
        this.dstChoiceBox.setValue(null);
    }
}