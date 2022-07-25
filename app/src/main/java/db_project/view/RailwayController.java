package db_project.view;

import java.util.List;

import db_project.pathFind.RouteHandler;
import db_project.view.viewUtils.StationWithCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class RailwayController {
    private final static double KM_FEE = 0.09;

    private ObservableList<StationWithCheckBox> stations;
    private final RouteHandler routeHandler = new RouteHandler();
    private List<String> selectedStations;
    private boolean selected = false;
    
    public RailwayController() {
        this.stations = FXCollections.observableArrayList();
    }
    
    public List<TableColumn<StationWithCheckBox, ?>> getTableViewColumns() {
        TableColumn<StationWithCheckBox, String> stationColumn = new TableColumn<>("Station");
        stationColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<StationWithCheckBox, CheckBox> checkStation = new TableColumn<>("Check");
        checkStation.setCellValueFactory(new PropertyValueFactory<>("select"));
        
        return List.of(stationColumn, checkStation);
    }

    public List<String> computePath(String srcStation, String dstStation) {
        return this.routeHandler.getStationsPath(srcStation, dstStation);
    }

    public void addSelectedStations(List<String> selectedStations) {
        this.selectedStations = selectedStations;
        this.selected = true;
    }

    /**
     * Compute total distance beetween last and first station in selected route
     * 
     * @return totale kilometers beetween the first station and the last
     * station selected
     * @throws IllegalAccessError if {@link db_project.view.RailwaController#addSelectedStations()}
     */
    public int getRouteDistance() {
        if (!this.selected) {
            throw new IllegalAccessError();
        }

        return this.routeHandler.getMinDistanceBetweenSrcToDst(
            this.selectedStations.get(0), 
            this.selectedStations.get(this.selectedStations.size() - 1)
        );
    }

    public double getPriceForSelectedRoute() {
        return this.getRouteDistance() * KM_FEE;
    }

    public void addStation(final StationWithCheckBox stat) {
        this.stations.add(stat);
    }

    public ObservableList<StationWithCheckBox> getStations() {
        return stations;
    }

    public List<String> getStationsNames() {
        return this.routeHandler.getStationsNames();
    }

    public RouteHandler getRouteHandler() {
        return routeHandler;
    }
}
