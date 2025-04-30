package apr.datastructure;

import java.util.ArrayList;
import java.util.List;

/**
 * Graph
 */
public class Graph<T> {

    public List<Node<T>> nodes;
    public List<Edge<T>> edges;

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void populate(int n, NodeGenerator<T> generator) {

    }
}
