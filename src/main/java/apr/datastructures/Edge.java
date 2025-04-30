package apr.datastructure;

/**
 * Edge
 */
public class Edge<T> {

    public double weight;
    Node<T> src, dest;

    public Edge(Node<T> src, Node<T> dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public String toString() {
        return String.format("Edge[%s -- (w: %.1f) --> %s]",
                src.content.toString(), weight, dest.content.toString());
    }
}
