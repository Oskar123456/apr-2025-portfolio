package apr.algorithms.graph;

import java.util.Map;

import apr.datastructures.graph.Node;

/**
 * GraphPaths
 */
public class GraphPaths<T> {

    public Node<T> dest;
    public Map<Node<T>, Double> dists;
    public Map<Node<T>, Node<T>> srcs;

    public GraphPaths(Node<T> dest, Map<Node<T>, Double> dists, Map<Node<T>, Node<T>> srcs) {
        this.dest = dest;
        this.srcs = srcs;
        this.dists = dists;
    }

    public String toString() {
        String str = String.format("%nDijkstra results (shortest path to %s):%n", dest.toString());
        for (var kv : dists.entrySet()) {
            var node = kv.getKey();
            var dist = kv.getValue();
            String distStr = dist == Double.POSITIVE_INFINITY ? "INF" : String.format("%3.1f", dist);
            str += String.format("\t%s (dist = %s)", node.toString(), distStr);
            if (dist != Double.POSITIVE_INFINITY) {
                str += " PATH: ";
                var pathNode = node;
                while (true) {
                    str += String.format("%s <-- ", pathNode.toString());
                    pathNode = srcs.get(pathNode);
                    if (pathNode == dest) {
                        str += String.format("%s", pathNode.toString());
                        break;
                    }
                }
            }
            str += System.lineSeparator();
        }
        return str;
    }
}
