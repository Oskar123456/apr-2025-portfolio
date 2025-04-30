package apr.algorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import apr.datastructures.Graph;
import apr.datastructures.Node;
import apr.datastructures.TwoTuple;

/**
 * Dijkstra
 */
public class Dijkstra {

    public static <T> TwoTuple<Map<Node<T>, Double>, Map<Node<T>, Node<T>>> use(Graph<T> graph, Node<T> src) {
        Map<Node<T>, Double> dists = new HashMap<>();
        Map<Node<T>, Node<T>> srcs = new HashMap<>();

        for (var node : graph.nodes) {
            dists.put(node, Double.POSITIVE_INFINITY);
        }

        PriorityQueue<TwoTuple<Node<T>, Double>> PQ = new PriorityQueue<>((a, b) -> a.second.compareTo(b.second));
        PQ.add(new TwoTuple<Node<T>, Double>(src, 0D));

        while (!PQ.isEmpty()) {
            var curTuple = PQ.remove();
            var curNode = curTuple.first;
            var curDist = curTuple.second;
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

        return new TwoTuple<Map<Node<T>, Double>, Map<Node<T>, Node<T>>>(dists, srcs);
    }
}
