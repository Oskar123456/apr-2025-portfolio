package apr.algorithms.graph.visualization;

import java.util.PriorityQueue;

import apr.datastructures.graph.TwoTuple;

/**
 * GraphSolver
 */
public class GraphSolver {

    public static <T> GraphReplay<T> astar(Graph<T> graph) {
        GraphReplay<T> replay = new GraphReplay<>(graph);

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

            for (var edge : graph.getNeighbors(curNode)) {
                if (edge.src != curNode) {
                    continue;
                }
                var neighbor = edge.dest;
                var neighborNewDist = curDist + edge.weight;
                if (graph.dists.get(neighbor) > neighborNewDist) {
                    graph.dists.put(neighbor, curDist + edge.weight);
                    graph.srcs.put(neighbor, curNode);
                    PQ.add(new TwoTuple<>(neighbor, neighborNewDist + neighbor.pos.dist(graph.dest.pos)));
                    replay.storeState(graph);
                }
            }
        }

        System.out.printf("astar solver done: src -> dest = %f%n", graph.dists.get(graph.dest));

        replay.seal(graph);
        return replay;
    }

    public static <T> GraphReplay<T> dijkstra(Graph<T> graph) {
        GraphReplay<T> replay = new GraphReplay<>(graph);

        PriorityQueue<TwoTuple<Node<T>, Double>> PQ = new PriorityQueue<>((a, b) -> a.second.compareTo(b.second));
        PQ.add(new TwoTuple<>(graph.src, 0D));

        System.out.println("dijkstra solver: ");
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

            for (var edge : graph.getNeighbors(curNode)) {
                if (edge.src != curNode) {
                    continue;
                }
                var neighbor = edge.dest;
                var neighborNewDist = curDist + edge.weight;
                if (graph.dists.get(neighbor) > neighborNewDist) {
                    graph.dists.put(neighbor, curDist + edge.weight);
                    graph.srcs.put(neighbor, curNode);
                    PQ.add(new TwoTuple<>(neighbor, neighborNewDist));
                    replay.storeState(graph);
                }
            }
        }

        System.out.printf("dijkstra solver done: src -> dest = %f%n", graph.dists.get(graph.dest));

        replay.seal(graph);
        return replay;
    }

    public interface HeuristicFn<T> {
        public double estimate(Node<T> a, Node<T> b);
    }

}
