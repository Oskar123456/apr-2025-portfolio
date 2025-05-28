package apr.examproj.alg;

import java.util.PriorityQueue;

import apr.datastructures.Pair;
import apr.examproj.ds.Graph;
import apr.examproj.ds.GraphNode;

/**
 * AStar
 */
public class AStar implements IPathFinder {

    @Override
    public <T> boolean search(Graph<T> graph, GraphNode<T> src, GraphNode<T> dest) {
        PriorityQueue<Pair<GraphNode<T>, Double>> PQ = new PriorityQueue<>(
                (a, b) -> a.second.compareTo(b.second));

        return !graph.dist(dest).equals(Double.POSITIVE_INFINITY);
    }

}
