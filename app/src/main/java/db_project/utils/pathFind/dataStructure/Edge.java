package db_project.utils.pathFind.dataStructure;

public class Edge {
    private int source;
    private int destination;
    private int weight;

    public Edge(final int src, final int dst, final int weight) {
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s): %d)", this.source, this.destination, this.weight);
    }
}
