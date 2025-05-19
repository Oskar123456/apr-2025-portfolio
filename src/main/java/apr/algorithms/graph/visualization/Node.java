package apr.algorithms.graph.visualization;

import apr.datastructures.graph.Point2D;

/**
 * Node
 */
public class Node<T> {

    public T data;
    public Point2D pos;

    public Node(T data) {
        this.data = data;
        this.pos = new Point2D(0, 0);
    }

    public Node(T data, Point2D pos) {
        this.data = data;
        this.pos = pos;
    }

    public String toString() {
        return String.format("Node<%s>[%s]", data.getClass().getSimpleName(), data.toString());
    }

    public interface Generator<V> {
        public V generate();
    }

}
