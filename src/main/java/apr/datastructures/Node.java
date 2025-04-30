package apr.datastructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Node
 */
public class Node<T> {

    public T content;
    public List<Edge<T>> edges;

    public Node(T t) {
        content = t;
        edges = new ArrayList<>();
    }

    public void connectTwoWays(Node<T> node, double weight) {
        node.edges.add(new Edge<>(node, this, weight));
        edges.add(new Edge<>(this, node, weight));
    }

    public String toString() {
        return String.format("Node[%s]", content.toString());
    }

    public List<Node<T>> getNeighbors() {
        List<Node<T>> neighbors = new ArrayList<>();
        for (Edge<T> e : edges) {
            neighbors.add(e.dest);
        }
        return neighbors;
    }

    public boolean isNeighbor(Node<T> other) {
        for (var edge : edges) {
            if (edge.dest == other) {
                return true;
            }
        }
        return false;
    }

    public boolean isConnected() {
        return false;
    }
}
