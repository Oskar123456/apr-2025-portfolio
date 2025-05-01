package apr.datastructures;

/**
 * AStarEdge
 */
public class AStarEdge<T> {
    public AStarNode<T> src, dest;

    public AStarEdge(AStarNode<T> src, AStarNode<T> dest) {
        this.src = src;
        this.dest = dest;
    }

    public String toString() {
        return String.format("Edge[%s -- %.1f --> %s]", src.data.toString(), src.dist(dest), dest.data.toString());
    }
}
