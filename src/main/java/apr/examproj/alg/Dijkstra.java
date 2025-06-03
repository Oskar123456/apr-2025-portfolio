package apr.examproj.alg;

import java.util.Comparator;
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
                Comparator.comparing(a -> a.second));

        System.out.println("Dijkstra.search(): " + graph.toString());

        graph.reset();

        PQ.add(new Pair<>(graph.getStart(), 0D));
        while (!PQ.isEmpty()) {
            var curPair = PQ.poll();
            var curNode = curPair.first;
            var curDist = curPair.second;
            if (graph.isVisited(curNode)) {
                continue;
            }
            graph.markAsVisited(curNode);
            if (curNode == graph.getDestination()) {
                break;
            }
            for (var edge : graph.edgesFrom(curNode)) {
                var neighbor = edge.dest;
                var dist = curDist + edge.weight;
                if (graph.dists.get(neighbor) > dist) {
                    graph.relax(curNode, neighbor, dist);
                    PQ.add(new Pair<>(neighbor, dist));
                }
            }
        }

        return !graph.dist(graph.getDestination()).equals(Double.POSITIVE_INFINITY);
    }

}
