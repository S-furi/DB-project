package db_project.pathFind.dataStructure;

import java.util.LinkedList;

public class Graph {
    private final int numOfVertices;
    private final LinkedList<Edge>[] adjList;    
    
    /**
     * Creates an empty graph.
     * 
     * @param numOfVertices
     */
    @SuppressWarnings("unchecked") //suppressing array type safety
    public Graph(final int numOfVertices) {
        this.numOfVertices = numOfVertices;
        this.adjList = new LinkedList[numOfVertices];
        
        //initialize adjacency list for all the vertices
        for (var i = 0; i < this.numOfVertices; i++) {
            this.adjList[i] = new LinkedList<>();
        }
    }

    public void addEdge(int source, int destination, int weight) {
        this.adjList[source].addFirst(new Edge(source, destination, weight));
    }

    public LinkedList<Edge>[] getAdjList() {
        return this.adjList;
    }

    public int getNumOfVertices() {
        return this.numOfVertices;
    }
}
