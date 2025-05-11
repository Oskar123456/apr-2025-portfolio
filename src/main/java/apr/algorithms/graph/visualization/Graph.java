package apr.algorithms.graph.visualization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Graph
 */
public class Graph<T> {

    public List<Node<T>> nodes;
    public List<Edge<T>> edges;

    public Map<Node<T>, Double> dists;
    public Map<Node<T>, Node<T>> srcs;
    public Set<Node<T>> visited;

    public Node<T> src, dest;

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        dists = new HashMap<>();
        srcs = new HashMap<>();
        visited = new HashSet<>();

        for (var node : nodes) {
            dists.put(node, Double.POSITIVE_INFINITY);
        }
    }

    public String toString() {
        String str = String.format("Graph(nodes: %d, edges: %d)", nodes.size(), edges.size());

        str += String.format("%nNodes:");
        for (var n : nodes) {
            str += String.format("%n\t%s", n.toString());
        }
        str += String.format("%nEdges:");
        for (var e : edges) {
            str += String.format("%n\t%s", e.toString());
        }

        return str;
    }
}
