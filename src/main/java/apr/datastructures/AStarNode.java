package apr.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * AStarNode
 */
public class AStarNode<T> {

    double x, y;
    T data;
    List<AStarEdge> edges;

    public AStarNode(T data, double x, double y) {
        edges = new ArrayList<>();
        this.x = x;
        this.y = y;
        this.data = data;
    }

    public double dist(AStarNode<T> other) {
        return Math.sqrt((x - other.x) * (x - other.x) + (y - other.y) * (y - other.y));
    }
}
