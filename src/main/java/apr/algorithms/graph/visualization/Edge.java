package apr.algorithms.graph.visualization;

/**
 * Edge
 */
public class Edge<T> {

    public Node<T> src, dest;
    public double weight;

    public Edge(Node<T> src, Node<T> dest) {
        this.src = src;
        this.dest = dest;
        this.weight = src.pos.dist(dest.pos);
    }

    public Edge(Node<T> src, Node<T> dest, double weight) {
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public String toString() {
        return String.format("Edge(%.1f)[%s, %s]", weight, src.toString(), dest.toString());
    }
}
