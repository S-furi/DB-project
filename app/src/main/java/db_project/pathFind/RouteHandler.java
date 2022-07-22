package db_project.pathFind;

import java.util.List;

import db_project.pathFind.dataHandling.StationsLoader;

public class RouteHandler {
    private final RoutePathFinder pathFinder;
    private final List<String> stations;
    private final StationsLoader stationsUtils;

    public RouteHandler() {
        this.pathFinder = new RoutePathFinder();
        this.stations = this.pathFinder.getStations();
        this.stationsUtils = new StationsLoader();
        this.stationsUtils.cacheDistancesData();
    }

    public int getMinDistanceBetweenSrcToDst(final String source, final String destination) {
        return this.pathFinder.getTotalDistance(source, destination);
    }
    
    public List<String> getStationsNames() {
        return stations;
    }

    public List<String> getStationsPath(String source, String destination) {
        return this.pathFinder.getPathFromSourceToDestination(source, destination);
    }
}
