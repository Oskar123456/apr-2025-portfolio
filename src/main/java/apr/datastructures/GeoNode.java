package apr.datastructures;

/**
 * Node
 */
public class GeoNode<T> extends Node<T> {

    public Point pos;

    public GeoNode(T t, Point pos) {
        super(t);
        this.pos = pos;
    }

    public double dist(GeoNode<T> other) {
        return pos.dist(other.pos);
    }
}
