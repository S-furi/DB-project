package db_project.utils.pathFind;

import java.util.PriorityQueue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import db_project.utils.pathFind.dataStructure.Graph;
import javafx.util.Pair;

public class PathFind {
    private final int numOfVertices;
    private final Graph graph;

    public PathFind(Graph graph) {
        this.graph = graph;
        this.numOfVertices = this.graph.getNumOfVertices();
    }
    
    /**
     * Applying Dijikstra algorithm, will be returned the shortest
     * path for each node in the graph.
     * 
     * @param sourceVertex
     * @return distances associated with every vertex
     */
    public int[] getMinDistance(int sourceVertex) {
        final var pq = this.getPriorityQueue();
        int[] dist = new int[this.numOfVertices];
        for(var i = 0; i < this.numOfVertices; i++) 
            dist[i] = Integer.MAX_VALUE;       

        pq.add(new Pair<Integer,Integer>(0, sourceVertex));
        dist[sourceVertex] = 0;
        while (!pq.isEmpty()) {
            int u = pq.poll().getValue();
            for(final var i : this.graph.getAdjList()[u]) {
                int v = i.getDestination();
                int w = i.getWeight();

                if (dist[v] > dist[u] + w) {
                    dist[v] = dist[u] + w;
                    pq.add(new Pair<Integer,Integer>(dist[v], v));
                }
            }
        }
        return dist;
    }

    private PriorityQueue<Pair<Integer, Integer>> getPriorityQueue() {
        return new PriorityQueue<>(this.numOfVertices,
                    new Comparator<Pair<Integer, Integer>>() {

                        @Override
                        public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
                            return p1.getKey() - p2.getKey();
                        }
                    });
    }
}
