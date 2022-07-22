package db_project.utils.pathFind;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    public Map<String,String> getPathFromSourceToDestination(String source, String destination) {
        Map<String, String> fromToEntries = new HashMap<>();
        int[] routes = this.pathFinder.getMinDistance(this.getStationAlias(source));
        
        final var parent = this.pathFinder.getParent();
        if (parent.isEmpty()) throw new IllegalAccessError();
        final var vertexTies = parent.get();

        for(var i = 0; i < this.numOfStations; i++) {
            String src = this.getStationNameFromAlias(i);
            if (vertexTies[i] != -1) {
                String dst = this.getStationNameFromAlias(vertexTies[i]);
                fromToEntries.put(src, dst);
            }
        }
        return fromToEntries;
    }

    private String getStationNameFromAlias(final int i) {
        return this.stationAliases
                .entrySet()
                .stream()
                .filter(t -> t.getValue() == i)
                .findAny()
                .get()
                .getKey();
    }

    public double getTotalDistance(String source, String destination) {
        return this.pathFinder
            .getMinDistance(this.getStationAlias(source))[this.getStationAlias(destination)];
    }

    private void generateStationAliases() {
        int i = 0;
        for (final var station : this.stationLoader.getStations()) {
            this.stationAliases.put(station, i);
            i++;
        }
    }
    
    private void initializeGraph() {
        this.stationLoader.getRouteInfo().forEach((k,v) -> {
                v.forEach(edge -> this.graph.addEdge(this.getStationAlias(k), 
                                                    this.getStationAlias(edge.getKey()), 
                                                    (int)Math.ceil(edge.getValue())));
        });
    }

    private int getStationAlias(String station) {
        return this.stationAliases.get(station);
    }
    
}
