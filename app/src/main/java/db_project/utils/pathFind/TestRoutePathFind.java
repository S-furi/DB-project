package db_project.utils.pathFind;

import java.util.LinkedList;
import java.util.Map;

public class TestRoutePathFind {
    public static void main(String[] args) {
        final String src = "NAPOLI CENTRALE";
        final String dst = "VENEZIA SANTA LUCIA";

        RoutePathFinder pathFinder = new RoutePathFinder();
        System.out.println(pathFinder.getTotalDistance(src, dst));
        var paths = pathFinder.getPathFromSourceToDestination(src, dst);
        
        LinkedList<String> lst = getOrderedPath(paths, src, dst);
        
        lst.forEach(t -> System.out.print(t + "->")); System.out.print("🏁");
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
}
