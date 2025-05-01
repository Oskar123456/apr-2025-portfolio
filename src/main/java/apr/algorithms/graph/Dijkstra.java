package apr.algorithms.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import apr.datastructures.graph.Graph;
import apr.datastructures.graph.Node;
import apr.datastructures.graph.TwoTuple;

/**
 * Dijkstra
 */
public class Dijkstra {

    public static <T> GraphPaths<T> wDefaults(Graph<T> graph, Node<T> src) {
        Map<Node<T>, Double> dists = new HashMap<>();
        Map<Node<T>, Node<T>> srcs = new HashMap<>();

        for (var node : graph.nodes) {
            dists.put(node, Double.POSITIVE_INFINITY);
        }
        dists.put(src, 0D);
        srcs.put(src, src);

        PriorityQueue<TwoTuple<Node<T>, Double>> PQ = new PriorityQueue<>((a, b) -> a.second.compareTo(b.second));
        PQ.add(new TwoTuple<Node<T>, Double>(src, 0D));

        int nVis = 0;
        while (!PQ.isEmpty() && nVis < graph.nodes.size()) {
            var curTuple = PQ.remove();
            var curNode = curTuple.first;
            var curDist = curTuple.second;
            ++nVis;
            for (var edge : curNode.edges) {
                var prevDist = dists.get(edge.dest);
                var newDist = curDist + edge.weight;
                if (prevDist > newDist) {
                    dists.put(edge.dest, newDist);
                    srcs.put(edge.dest, curNode);
                    PQ.add(new TwoTuple<Node<T>, Double>(edge.dest, newDist));
                }
            }
        }

        return new GraphPaths<>(src, dists, srcs);
    }
}
