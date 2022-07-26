package db_project.pathFind.tests;

import db_project.pathFind.RoutePathFinder;

public class TestRoutePathFind {
    public static void main(String[] args) {
        final String src = "LAMEZIA";
        final String dst = "VENEZIA SANTA LUCIA";

        RoutePathFinder pathFinder = new RoutePathFinder();
        System.out.println(pathFinder.getTotalDistance(src, dst));

        pathFinder.getPathFromSourceToDestination(src, dst)
                .forEach(t -> System.out.print(t + "->"));
        System.out.print("🏁");
    }
}
