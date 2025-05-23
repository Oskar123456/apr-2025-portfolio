package apr.examproj.ds;

import apr.datastructures.graph.Point2D;

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

    public double dist(GraphNode<T> other) {
        return position.dist(other.position);
    }

}
