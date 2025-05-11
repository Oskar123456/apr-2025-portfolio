package apr.algorithms.graph.visualization;

import java.util.List;

import apr.datastructures.graph.Point2D;

/**
 * Node
 */
public class Node<T> {

    public T data;
    public Point2D pos;

    public Node(T data, Point2D pos) {
        this.data = data;
        this.pos = pos;
    }

    public String toString() {
        return String.format("Node<%s>[%s]", data.getClass().getSimpleName(), data.toString());
    }
}
