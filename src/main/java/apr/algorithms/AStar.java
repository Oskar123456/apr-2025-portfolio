package apr.algorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import apr.datastructures.GeoNode;
import apr.datastructures.Graph;
import apr.datastructures.Node;
import apr.datastructures.TwoTuple;

/**
 * AStar
 */
public class AStar {

    public static <T> GraphPaths<T> use(Graph<T> graph, GeoNode<T> src, GeoNode<T> dest, DistEstimator<T> estim) {
        Map<Node<T>, Double> dists = new HashMap<>();
        Map<Node<T>, Node<T>> srcs = new HashMap<>();

        for (var node : graph.nodes) {
            dists.put(node, Double.POSITIVE_INFINITY);
        }

        PriorityQueue<TwoTuple<Node<T>, Double>> PQ = new PriorityQueue<>((a, b) -> a.second.compareTo(b.second));
        PQ.add(new TwoTuple<Node<T>, Double>(src, estim.lowerBound(src, dest)));

        while (!PQ.isEmpty()) {
            var curTuple = PQ.remove();
            var curNode = curTuple.first;
            var curDist = curTuple.second;

            if (curNode == dest) {
                return new GraphPaths<>(src, dists, srcs);
            }

            for (var edge : curNode.edges) {
                var prevDist = dists.get(edge.dest);
                var newDist = curDist + edge.weight;
                if (prevDist > newDist) {
                    dists.put(edge.dest, newDist);
                    srcs.put(edge.dest, curNode);
                    PQ.add(new TwoTuple<Node<T>, Double>(edge.dest,
                            newDist + estim.lowerBound((GeoNode<T>) edge.dest, dest)));
                }
            }
        }

        return null;
    }

    public interface DistEstimator<T> {
        public double lowerBound(GeoNode<T> src, GeoNode<T> dest);
    }
}
