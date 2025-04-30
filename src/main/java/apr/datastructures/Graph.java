package apr.datastructure;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
        Random rng = new Random(System.currentTimeMillis());
        nodes = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            nodes.add(generator.make());
        }

        Set<Node<T>> visited = new HashSet<>();
        Deque<Node<T>> stack = new ArrayDeque<>();

        stack.add(nodes.get(rng.nextInt(0, n)));
        while (!stack.isEmpty()) {
            var curNode = stack.pop();
            visited.add(curNode);
            int nEdges = rng.nextInt(1, (int) Math.sqrt(n));
            for (int i = 0; i < nEdges; i++) {
                int r = rng.nextInt(0, n);
                Node<T> neighbor = nodes.get(r);
                int j = 0;
                while (j++ < n && visited.contains(neighbor)) {
                    r = (r + 1) % n;
                    neighbor = nodes.get(r);
                }
                if (j == n) {
                    break;
                }
                double weight = rng.nextInt(1, 25);
                curNode.connectTwoWays(neighbor, weight);
                edges.add(new Edge<T>(curNode, neighbor, weight));
            }
        }
    }

    public String toString() {
        String str = String.format("Graph(nodes: %d, edges: %d)", nodes.size(), edges.size());

        str += String.format("%nNodes:");
        for (var n : nodes) {
            str += String.format("%n%s", n.toString());
        }
        str += String.format("%nEdges:");
        for (var e : edges) {
            str += String.format("%n%s", e.toString());
        }

        return str;
    }
}
