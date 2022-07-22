package db_project.pathFind;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import db_project.pathFind.dataHandling.StationsLoader;
import db_project.pathFind.dataStructure.Graph;

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

    public LinkedList<String> getPathFromSourceToDestination(String source, String destination) {
        Map<String, String> fromToEntries = new HashMap<>();
        this.pathFinder.getMinDistance(this.getStationAlias(source));

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
        
        return getOrderedPath(fromToEntries, source, destination);
    }

    /**
     * Recursive lookup onto parent map for finding shortest route.
     * 
     * @param parent (child, parent) map
     * @param arrive Destination station's name
     * @param actual Actual station's name
     * @return an ordered list of station's names describing a shortest path.
     */
    private static LinkedList<String> getOrderedPath(final Map<String, String> parent, final String arrive, final String actual) {
        if (arrive.equals(actual)) {
            var list = new LinkedList<String>();
            list.addLast(actual);
            return list;
        } else {
            var lst = getOrderedPath(parent, arrive, parent.get(actual));
            lst.addLast(actual);
            return lst;
        }
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

    public int getTotalDistance(String source, String destination) {
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

    public List<String> getStations() {
        return this.stationLoader.getStations();
    }
}
