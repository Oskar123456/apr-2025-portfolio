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
    Map<Node<T>, List<Edge<T>>> nodeEdges;

    public Map<Node<T>, Double> dists;
    public Map<Node<T>, Double> guesstimates;
    public Map<Node<T>, Node<T>> srcs;
    public Set<Node<T>> visited;
    public Set<Edge<T>> path;

    public Node<T> src, dest;

    public Graph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        nodeEdges = new HashMap<>();
        dists = new HashMap<>();
        guesstimates = new HashMap<>();
        srcs = new HashMap<>();
        visited = new HashSet<>();
        path = new HashSet<>();

        for (var node : nodes) {
            dists.put(node, Double.POSITIVE_INFINITY);
        }
    }

    public List<Edge<T>> getNeighbors(Node<T> node) {
        return nodeEdges.get(node);
    }

    public void remove(Node<T> node) {
        nodes.remove(node);
        List<Edge<T>> edgesToRemove = new ArrayList<>();
        for (var edge : edges) {
            if (edge.src == node || edge.dest == node) {
                edgesToRemove.add(edge);
            }
        }
        nodeEdges.remove(node);
        edges.removeAll(edgesToRemove);
        dists.remove(node);
        guesstimates.remove(node);
        srcs.remove(node);
        visited.remove(node);
        if (src == node) {
            src = null;
        }
        if (dest == node) {
            dest = null;
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

    public void addNode(Node<T> node) {
        nodes.add(node);
        nodeEdges.put(node, new ArrayList<>());
    }

    public void addEdge(Edge<T> edge) {
        for (var e : edges) {
            if (e.src == edge.src && e.dest == edge.dest) {
                return;
            }
        }
        edges.add(edge);
        if (nodeEdges.containsKey(edge.src))
            nodeEdges.put(edge.src, new ArrayList<>());
        nodeEdges.get(edge.src).add(edge);
    }
}
