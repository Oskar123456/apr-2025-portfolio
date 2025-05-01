package apr.datastructures.graph;

import java.util.ArrayList;
import java.util.List;

/**
 * AStarNode
 */
public class AStarNode<T> {

    public double x, y;
    public T data;
    public List<AStarEdge<T>> edges;

    public AStarNode(T data, double x, double y) {
        edges = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.data = data;
    }

    public double dist(AStarNode<T> other) {
        return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
    }

    public String toString() {
        return String.format("Node[%s (%.1f, %.1f)]", data.toString(), x, y);
    }

    public boolean isConnectedTo(AStarNode<T> other) {
        for (var edge : edges) {
            if (edge.dest == other) {
                return true;
            }
            for (var edge2 : edge.dest.edges) {
                if (edge2.dest == this) {
                    return true;
                }
            }
        }
        return false;
    }
}
