package apr.examproj.ds;

import apr.datastructures.graph.Point2D;
import apr.examproj.utils.Stringify;

/**
 * GraphNode
 */
public class GraphNode<T> {

    public T data;
    public Point2D position;

    public GraphNode(T data) {
        this.data = data;
        position = new Point2D(0, 0);
    }

    public GraphNode(T data, Point2D position) {
        this.data = data;
        this.position = position;
    }

    public GraphNode(T data, double x, double y) {
        this.data = data;
        this.position = new Point2D(x, y);
    }

    public double dist(GraphNode<T> other) {
        return position.dist(other.position);
    }

    @Override
    public String toString() {
        return Stringify.toString(this);
    }

}
