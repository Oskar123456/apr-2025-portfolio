package apr.algorithms.graph.visualization;

import java.util.PriorityQueue;

import apr.datastructures.graph.TwoTuple;

/**
 * GraphSolver
 */
public class GraphSolver {

    public static <T> GraphReplay<T> astar(Graph<T> graph, HeuristicFn<T> H) {
        GraphReplay<T> replay = new GraphReplay<>(graph);

        graph.visited.clear();
        graph.srcs.clear();
        graph.path.clear();
        for (var node : graph.nodes) {
            graph.dists.put(node, Double.POSITIVE_INFINITY);
        }
        graph.dists.put(graph.src, 0D);

        PriorityQueue<TwoTuple<Node<T>, Double>> PQ = new PriorityQueue<>((a, b) -> a.second.compareTo(b.second));
        PQ.add(new TwoTuple<>(graph.src, 0D));

        System.out.println("astar solver: ");
        System.out.println(graph.toString());

        while (!PQ.isEmpty()) {
            var curTuple = PQ.remove();
            var curNode = curTuple.first;
            var curDist = graph.dists.get(curNode);

            graph.visited.add(curNode);
            replay.storeState(graph);

            if (curNode == graph.dest) {
                break;
            }

            for (var edge : graph.edges) {
                if (edge.src != curNode) {
                    continue;
                }
                var neighbor = edge.dest;
                var neighborNewDist = curDist + edge.weight;
                if (graph.dists.get(neighbor) > neighborNewDist) {
                    graph.dists.put(neighbor, curDist + edge.weight);
                    graph.srcs.put(neighbor, curNode);
                    PQ.add(new TwoTuple<>(neighbor, neighborNewDist + H.estimate(neighbor, graph.dest)));
                    replay.storeState(graph);
                }
            }
        }

        var node = graph.dest;
        while (graph.dists.containsKey(node)) {
            var nodeSrc = graph.srcs.get(node);
            for (var edge : graph.edges) {
                if (edge.dest == node && edge.src == nodeSrc) {
                    graph.path.add(edge);
                    break;
                }
            }
            node = nodeSrc;
        }
        replay.storeState(graph);

        System.out.printf("astar solver done: src -> dest = %f%n", graph.dists.get(graph.dest));

        return replay;
    }

    public interface HeuristicFn<T> {
        public double estimate(Node<T> a, Node<T> b);
    }

}
