package db_project.utils.pathFind.dataStructure;

public class Edge {
    private int source;
    private int destination;
    private double weight;

    public Edge(final int src, final int dst, final double weight) {
        this.source = src;
        this.destination = dst;
        this.weight = weight;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
