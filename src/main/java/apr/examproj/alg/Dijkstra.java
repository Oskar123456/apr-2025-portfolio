package apr.examproj.alg;

import java.util.PriorityQueue;

import apr.datastructures.Pair;
import apr.examproj.ds.Graph;
import apr.examproj.ds.GraphNode;

/**
 * Dijkstra
 */
public class Dijkstra<T> implements PathFinder<T> {

    @Override
    public boolean search(Graph<T> graph) {
        PriorityQueue<Pair<GraphNode<T>, Double>> PQ = new PriorityQueue<>(
                (a, b) -> a.second.compareTo(b.second));

        System.out.println("Dijkstra.search(): " + graph.toString());

        graph.reset();

        PQ.add(new Pair<GraphNode<T>, Double>(graph.getStart(), 0D));
        while (!PQ.isEmpty()) {
            var curPair = PQ.remove();
            var curNode = curPair.first;
            var curDist = curPair.second;
            graph.markAsVisited(curNode);
            if (curNode == graph.getDestination()) {
                break;
            }
            for (var edge : graph.edgesFrom(curNode)) {
                var neighbor = edge.dest;
                var dist = curDist + edge.weight;
                if (graph.dists.get(neighbor) > dist) {
                    graph.dists.put(neighbor, dist);
                    graph.srcs.put(neighbor, curNode);
                    PQ.add(new Pair<GraphNode<T>, Double>(neighbor, dist));
                }
            }
        }

        return !graph.dist(graph.getDestination()).equals(Double.POSITIVE_INFINITY);
    }

}
