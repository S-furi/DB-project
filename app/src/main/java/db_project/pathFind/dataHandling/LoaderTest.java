package db_project.pathFind.dataHandling;

import java.util.List;
import java.util.Map;

import javafx.util.Pair;

public class LoaderTest {
    private static StationsLoader stLoad;
    public static void main(String[] args) {
        stLoad = new StationsLoader();
        stLoad.cacheDistancesData();
        printRouteInfo(stLoad.getRouteInfo());
    }

    @SuppressWarnings ("unused")
    private static void printStations() {
        stLoad.getStations().forEach(System.out::println);
    }

    //@SuppressWarnings ("unused")
    private static void printRouteInfo(final Map<String, List<Pair<String, Double>>> routeInfo) {
        routeInfo.forEach((k, v) -> {
            v.forEach(t -> {
                System.out.println(String.format("(%s,%s):%f", k, t.getKey(), t.getValue()));
            });
        });
    }
}
