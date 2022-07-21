package db_project.utils.pathFind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.checkerframework.checker.units.qual.K;

import db_project.utils.pathFind.dataHandling.StationsLoader;
import db_project.utils.pathFind.dataStructure.Graph;
import javafx.util.Pair;

public class RoutePathFinder {
    private final PathFind pathFinder;
    private final StationsLoader stationLoader;
    private final Graph graph;
    private final int numOfStations;
    private final Map<String, Integer> stationAliases;

    public RoutePathFinder() {
        this.stationAliases = new HashMap<>();
        this.stationLoader = new StationsLoader();
        this.stationLoader.cacheDistancesData();
        this.numOfStations = this.stationLoader.getStations().size();
        this.graph = new Graph(this.numOfStations);

        this.generateStationAliases();
        this.initializeGraph();

        this.pathFinder = new PathFind(this.graph);
    }

    public List<Pair<String, String>> getPathFromSourceToDestination(String source, String destination) {
        double[] distances = this.pathFinder.getMinDistance(this.getStationAlias(source));
        return null;
    }

    public double getTotalDistance(String source, String destination) {
        return this.pathFinder
                .getMinDistance(this.getStationAlias(source))[this.getStationAlias(destination)];
    }

    private void generateStationAliases() {
        this.stationLoader.getStations().forEach(t -> {
            for (var i = 0; i < this.numOfStations; i++) {
                this.stationAliases.put(t, i);
            }
        });
    }
    
    private void initializeGraph() {
        this.stationLoader.getRouteInfo().forEach((k,v) -> {
                v.forEach(edge -> this.graph.addEdge(this.getStationAlias(k), 
                                                    this.getStationAlias(edge.getKey()), 
                                                    edge.getValue()));
        });
    }

    private int getStationAlias(String station) {
        return this.stationAliases.get(station);
    }
    
}
