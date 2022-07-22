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
    private ObservableList<StationWithCheckBox> stations;
    private final RouteHandler routeHandler = new RouteHandler();
    
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
