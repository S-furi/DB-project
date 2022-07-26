package db_project.pathFind.tests;

import db_project.pathFind.PathFind;
import db_project.pathFind.dataStructure.Graph;

public class TestPathFind {
    public static void main(String[] args) {
        final int numOfVertices = 6;
        final int sourceVertex = 2;
        final var g = new Graph(numOfVertices);

        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 3);
        g.addEdge(1, 2, 1);
        g.addEdge(1, 3, 2);
        g.addEdge(2, 3, 4);
        g.addEdge(3, 4, 2);
        g.addEdge(4, 5, 6);

        PathFind pathFinder = new PathFind(g);
        var distances = pathFinder.getMinDistance(sourceVertex);
        printShortestPath(distances, numOfVertices, sourceVertex);
    }

    public static void printShortestPath(int[] distances, int numOfVertices, int sourceVertex) {
        for (var i = 0; i < numOfVertices; i++) {
            System.out.println("Source vertex: " + sourceVertex + "to vertex "
                    + i + ", distance: " + distances[i]);
        }
    }
}
