package apr.datastructures;

/**
 * AStarEdge
 */
public class AStarEdge<T> {
    public AStarNode<T> src, dest;
    public double weight;

    public AStarEdge(AStarNode<T> src, AStarNode<T> dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }
}
