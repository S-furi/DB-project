package db_project.view;

import java.net.URL;
import java.util.ResourceBundle;

import db_project.db.dbGenerator.DBGenerator;
import db_project.view.controller.PathController;
import db_project.view.controller.SectionController;
import db_project.view.controller.PathController.TripSolution;
import db_project.view.controller.SectionController.PathDetail;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;

public class DbRailwaController implements Initializable {
    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox<String> pathCheckBox;
    @FXML private ChoiceBox<?> trainCheckBox;
    @FXML private ChoiceBox<String> srcStationCheckBox;
    @FXML private ChoiceBox<String> dstStationCheckBox;

    @FXML private TableView<?> resultTableView;
    @FXML private TableView<PathDetail> pathDetailTableView = new TableView<>();
    @FXML private TableView<TripSolution> tripSolutionsTableView = new TableView<>();
    @FXML private Button routeConfirmationButton;
    @FXML private Button confirmPathButton;

    private DBGenerator dbGenerator;
    private PathController pathController;
    private SectionController sectionController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.dbGenerator = new DBGenerator();
        this.pathController = new PathController(this.dbGenerator);
        this.sectionController = new SectionController(dbGenerator);
        this.fillTableViews();
        this.fillCheckBoxes();
    }

    
    private void fillTableViews() {
        this.fillPathTableView();
        this.fillSectionTableView();
    }

    private void fillPathTableView() {
        this.tripSolutionsTableView.setEditable(true);
        this.pathController
            .getTableViewColumns()
            .forEach(t -> this.tripSolutionsTableView.getColumns().add(t));
        
        this.tripSolutionsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.tripSolutionsTableView.setItems(this.pathController.getTripSolutions());
        this.tripSolutionsTableView.refresh();
    }

    private void fillSectionTableView() {
        this.pathDetailTableView.setEditable(true);
        this.sectionController
            .getTableViewColumns()
            .forEach(t -> this.pathDetailTableView.getColumns().add(t));
        
        this.pathDetailTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        this.pathDetailTableView.setItems(this.sectionController.getPathDetails());
        this.pathDetailTableView.refresh();
    }
    
    private void fillCheckBoxes() {
        this.pathCheckBox.getItems().setAll(this.pathController.getAllPathCodes());
        this.srcStationCheckBox.getItems().setAll(this.pathController.getStations());
        this.dstStationCheckBox.getItems().setAll(this.pathController.getStations());
    }

    @FXML
    void saveAndConvertCurrentDate(ActionEvent event) {
    }

    @FXML
    void saveRouteInfo(ActionEvent event) {

    }

    /**
     * Find the route from a to b, specifing all the paths (as in Controller.java)
     * @param event
     */
    @FXML
    void getSelectedPathSections(ActionEvent event) {
        final var pathId = 
            this.pathController.getPathFromStations(
                    this.srcStationCheckBox.getValue(),
                    this.dstStationCheckBox.getValue()).get();

        this.sectionController.computeSectionsFromPath(pathId);
        this.pathDetailTableView.refresh();
    }
}
