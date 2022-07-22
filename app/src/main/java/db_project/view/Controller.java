package db_project.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import db_project.db.ConnectionProvider;
import db_project.db.tables.UserTable;
import db_project.model.User;
import db_project.pathFind.RouteHandler;
import db_project.utils.SampleLoader;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
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
    private Button searchSolutionsButton;

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

    @FXML
    private ChoiceBox<String> dstChoiceBox = new ChoiceBox<>();

    @FXML
    private ChoiceBox<String> srcChoiceBox = new ChoiceBox<>();

    
    /* Railway Soulutions Table */
    @FXML
    private TableView<StationWithCheckBox> solutionsTable;
    private ObservableList<StationWithCheckBox> stations = FXCollections.observableArrayList();
    @FXML
    private Button saveSolutionsButton;
    
    private ObservableList<User> users = FXCollections.observableArrayList();
    private final static ConnectionProvider connectionProvider =  ConnectionController.getConnectionProvider();
    private final static UserTable users_table = new UserTable(connectionProvider.getMySQLConnection());
    private final SampleLoader sample = new SampleLoader("data_generation/users/user_data/db_data.json");
    private final RouteHandler routeHandler = new RouteHandler();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.createUsersTableView();
        this.createStationsTableView();
        this.resetSqlTable();

        this.register_button.disableProperty().bind(
            Bindings.isEmpty(firstname_field.textProperty())
            .or(Bindings.isEmpty(lastname_field.textProperty()))
            .or(Bindings.isEmpty(tel_field.textProperty()))
            .or(Bindings.isEmpty(email_field.textProperty()))
        );

        //Doesn't work
        this.searchSolutionsButton.disableProperty().bind(
            this.srcChoiceBox.valueProperty().isNull()
            .or(dstChoiceBox.valueProperty().isNull())
        );

        this.saveSolutionsButton.setDisable(true);

        sample.getStoredPeople().forEach(t -> this.addUserToCurrentTable(t));

        this.populateCheckBox();
        
        System.out.println("Setup completed: " + this.resetSqlTable());
    }

    private void populateCheckBox() {
        this.srcChoiceBox.getItems().setAll(this.routeHandler.getStationsNames());
        this.dstChoiceBox.getItems().setAll(this.routeHandler.getStationsNames());
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

    private void createStationsTableView() {
        this.solutionsTable.setEditable(true);
        
        TableColumn<StationWithCheckBox, String> stationColumn = new TableColumn<>("Station");
        stationColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<StationWithCheckBox, CheckBox> checkStation = new TableColumn<>("Check");
        checkStation.setCellValueFactory(new PropertyValueFactory<>("select"));

        var columns = List.of(stationColumn, checkStation);
        columns.forEach(t -> this.solutionsTable.getColumns().add(t));

        this.solutionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.solutionsTable.setItems(this.stations);
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
        List<String> path = this.routeHandler.getStationsPath(srcStation, dstStation);
        path.forEach(t -> this.stations.add(new StationWithCheckBox(t)));
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
        final var stations = this.solutionsTable
            .getItems()
            .stream()
            .filter(t -> t.getSelect().isSelected())
            .map(t -> t.getName())
            .collect(Collectors.toList());
        stations.forEach(System.out::println);
        this.solutionsTable.getItems().clear();
        this.srcChoiceBox.setValue(null);
        this.dstChoiceBox.setValue(null);
    }

    public class StationWithCheckBox {
        private String name;
        private CheckBox select;
        
        public StationWithCheckBox(String name) {
            this.name = name;
            this.select = new CheckBox();
            this.select.setSelected(true);
        }

        public CheckBox getSelect() {
            return this.select;
        }

        public void setSelect(CheckBox select) {
            this.select = select;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}