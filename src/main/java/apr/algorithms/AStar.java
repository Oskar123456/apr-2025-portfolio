package apr.algorithms;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import apr.datastructures.AStarGraph;
import apr.datastructures.AStarNode;
import apr.datastructures.TwoTuple;

/**
 * AStar
 */
public class AStar {

    public static <T> AStarGraphPath<T> use(AStarGraph<T> graph, AStarNode<T> src, AStarNode<T> dest) {
        System.out.printf("AStar:: %s --> %s%n%n", src.toString(), dest.toString());

        Map<AStarNode<T>, Double> dists = new HashMap<>();
        Map<AStarNode<T>, AStarNode<T>> srcs = new HashMap<>();

        for (var node : graph.nodes) {
            dists.put(node, Double.POSITIVE_INFINITY);
        }
        dists.put(src, 0D);

        PriorityQueue<TwoTuple<AStarNode<T>, Double>> PQ = new PriorityQueue<>((a, b) -> a.second.compareTo(b.second));
        PQ.add(new TwoTuple<AStarNode<T>, Double>(src, src.dist(dest)));

        while (!PQ.isEmpty()) {
            var curTuple = PQ.remove();
            var curNode = curTuple.first;
            var curDist = dists.get(curNode);
            if (curNode == dest) {
                System.out.printf("%n%n >>> AStar:: Path with length %.1f found: ", dists.get(dest));
                String str = "";
                AStarNode<T> node = curNode;
                while (curNode != null) {
                    str += curNode.toString() + " <- ";
                    curNode = srcs.get(curNode);
                }
                System.out.println(str);
                System.out.println();
                break;
            }
            for (var edge : curNode.edges) {
                var neighbor = edge.dest;
                var newDist = curDist + curNode.dist(neighbor);
                var oldDist = dists.get(neighbor);
                if (oldDist > newDist) {
                    dists.put(neighbor, newDist);
                    srcs.put(neighbor, curNode);
                    PQ.add(new TwoTuple<AStarNode<T>, Double>(neighbor, newDist + neighbor.dist(dest)));
                }
            }
        }

        System.out.printf("%n%n >>> AStar:: No path found...%n%n");
        return null;
    }
}
