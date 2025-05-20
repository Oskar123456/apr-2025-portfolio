package apr.algorithms.graph.visualization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import apr.datastructures.graph.Point2D;
import apr.datastructures.graph.Point2DI;

/**
 * Graph
 */
public class Graph<T> {

    public List<Node<T>> nodes = new ArrayList<>();
    public List<Edge<T>> edges = new ArrayList<>();
    Map<Node<T>, List<Edge<T>>> nodeEdges = new HashMap<>();

    public Map<Node<T>, Double> dists = new HashMap<>();
    public Map<Node<T>, Double> guesstimates = new HashMap<>();
    public Map<Node<T>, Node<T>> srcs = new HashMap<>();
    public Set<Node<T>> visited = new HashSet<>();
    public Set<Edge<T>> path = new HashSet<>();

    public Node<T> src, dest;

    public Graph() {
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
        if (edge.src == edge.dest) {
            return;
        }

        for (var e : edges) {
            if (e.src == edge.src && e.dest == edge.dest) {
                return;
            }
        }

        edges.add(edge);
        if (!nodeEdges.containsKey(edge.src)) {
            nodeEdges.put(edge.src, new ArrayList<>());
        }
        nodeEdges.get(edge.src).add(edge);
    }

    public static <V> Graph<V> makeGridGraph(int width, int height, Node.Generator<V> generator) {
        Random rng = new Random();
        Graph<V> graph = new Graph<>();

        List<List<Node<V>>> nodes = new ArrayList<>();

        double w = 1D / (width + 1D);
        double h = 1D / (height + 1D);
        for (int i = 0; i < height; i++) {
            nodes.add(new ArrayList<>());
            for (int j = 0; j < width; j++) {
                Node<V> node = new Node<>(generator.generate(),
                        new Point2D(j * w + w, i * h + h));
                nodes.get(i).add(node);
                graph.addNode(node);
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int nEdges = rng.nextInt(4, 8);
                for (int k = 0; k < nEdges; k++) {
                    Point2DI dir = Point2DI.dirsIncDiag[rng.nextInt(0, 8)];
                    Point2DI p = new Point2DI(j, i).add(dir);
                    if (p.x >= 0 && p.y >= 0 && p.x < width && p.y < height) {
                        graph.addEdge(new Edge<>(nodes.get(i).get(j), nodes.get(p.y).get(p.x)));
                    }
                }
            }
        }

        return graph;
    }

}
