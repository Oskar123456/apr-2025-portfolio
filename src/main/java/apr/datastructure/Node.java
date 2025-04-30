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

    public String toString() {
        return String.format("Node[%s]", content.toString());
    }

    public boolean isConnectedWith(Node<T> other) {
        for (var edge : edges) {
            if (edge.dest == other) {
                return true;
            }
        }
        return false;
    }
}
