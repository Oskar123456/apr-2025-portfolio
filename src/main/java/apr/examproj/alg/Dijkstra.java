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

        // for (var edge : graph.getEdges()) {
        // System.out.println("Dijkstra.search(): " + edge);
        // }

        PQ.add(new Pair<GraphNode<T>, Double>(graph.getStart(), 0D));
        while (!PQ.isEmpty()) {
            var curPair = PQ.remove();
            var curNode = curPair.first;
            var curDist = curPair.second;
            graph.markAsVisited(curNode);
            // System.out.println("Dijkstra.search(): visiting " + curNode + " it has " +
            // graph.edgesFrom(curNode).size()
            // + " out edges");
            if (curNode == graph.getDestination()) {
                break;
            }
            for (var edge : graph.edgesFrom(curNode)) {
                var neighbor = edge.dest;
                var dist = curDist + edge.weight;
                if (dist < 0 || graph.dist(neighbor) < 0 || edge.weight < 0) {
                    System.out.println("Dijkstra.search() : negative weight");
                    System.exit(1);
                }
                if (graph.dist(neighbor) > dist) {
                    graph.updateDist(neighbor, dist);
                    graph.updateSrcs(edge);
                    graph.markAsSeen(neighbor);
                    PQ.add(new Pair<GraphNode<T>, Double>(neighbor, dist));
                }
            }
        }

        return !graph.dist(graph.getDestination()).equals(Double.POSITIVE_INFINITY);
    }

}
