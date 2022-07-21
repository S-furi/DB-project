package db_project.utils.pathFind;

import java.util.PriorityQueue;
import java.util.Comparator;

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
        boolean[] spt = new boolean[this.numOfVertices];
        int[] distance = new int[this.numOfVertices];

        //Initialize all the vertices to INF (MAX_INT)
        for (var i = 0; i < this.numOfVertices; i++) {
            distance[i] = Integer.MAX_VALUE;
        }

        final var pq = this.getPriorityQueue();        
        
        distance[0] = 0;
        Pair<Integer, Integer> p0 = new Pair<>(distance[0], 0);
        pq.offer(p0);

        while(!pq.isEmpty()) {
            Pair<Integer, Integer> extracedPair = pq.poll();
            int extractedVertex = extracedPair.getValue();
            if (spt[extractedVertex]) { continue; }
            
            spt[extractedVertex] = true;
            final var edges = this.graph.getAdjList()[extractedVertex];
            
            for (final var edge : edges) {
                if (spt[edge.getDestination()]) { continue; }

                var newKey = distance[extractedVertex] + edge.getWeight();
                var currentKey = distance[edge.getDestination()];
                if (currentKey > newKey) {
                    var p = new Pair<Integer, Integer>(newKey, edge.getDestination());
                    pq.offer(p);
                    distance[edge.getDestination()] = newKey;
                }
            }
        }
        return distance;
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
