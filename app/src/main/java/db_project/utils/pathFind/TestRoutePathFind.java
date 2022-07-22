package db_project.utils.pathFind;

public class TestRoutePathFind {
    public static void main(String[] args) {
        RoutePathFinder pathFinder = new RoutePathFinder();
        System.out.println(pathFinder.getTotalDistance("BOLOGNA C.LE", "REGGIO EMILIA"));
    }
}
