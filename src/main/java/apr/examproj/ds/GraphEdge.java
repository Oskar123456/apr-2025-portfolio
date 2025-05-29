package apr.examproj.ds;

import java.util.List;

import apr.examproj.utils.Stringify;

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

    @Override
    public String toString() {
        return Stringify.toString(this);
    }

    public static <T> GraphEdge<T> findMin(List<GraphEdge<T>> edges) {
        GraphEdge<T> minEdge = null;
        double min = Double.POSITIVE_INFINITY;
        for (var edge : edges) {
            if (edge.weight < min) {
                min = edge.weight;
                minEdge = edge;
            }
        }
        return minEdge;
    }

}
