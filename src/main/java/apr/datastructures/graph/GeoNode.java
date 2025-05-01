package apr.datastructures.graph;

/**
 * Node
 */
public class GeoNode<T> extends Node<T> {

    public Point2D pos;

    public GeoNode(T t, Point2D pos) {
        super(t);
        this.pos = pos;
    }

    public double dist(GeoNode<T> other) {
        return pos.dist(other.pos);
    }

    public String toString() {
        return String.format("Node[%s (%.1f,%.1f)]", content.toString(), pos.x, pos.y);
    }
}
