package apr.examproj.ds;

/**
 * GraphEdge
 */
public class GraphEdge<T> {

    public GraphNode<T> src, dest;
    public double weight;

    public GraphEdge(GraphNode<T> src, GraphNode<T> dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

}
