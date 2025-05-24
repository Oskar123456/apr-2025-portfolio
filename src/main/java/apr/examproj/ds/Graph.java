package apr.examproj.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Graph
 */
public class Graph<T> {

    List<GraphEdge<T>> edges = new ArrayList<>();
    List<GraphNode<T>> nodes = new ArrayList<>();
    Map<GraphNode<T>, List<GraphNode<T>>> neighbors = new HashMap<>();
    Map<GraphNode<T>, List<GraphEdge<T>>> outgoingEdges = new HashMap<>();

    public Graph() {
    }

    public List<GraphNode<T>> neighbors(GraphNode<T> node) {
        return neighbors.get(node);
    }

    public List<GraphEdge<T>> paths(GraphNode<T> node) {
        return outgoingEdges.get(node);
    }

    public void addNode(GraphNode<T> node) {
        nodes.add(node);
        neighbors.put(node, new ArrayList<>());
    }

    public void addEdge(GraphEdge<T> edge) {
        edges.add(edge);
        neighbors.get(edge.src).add(edge.dest);
        outgoingEdges.get(edge.src).add(edge);
    }

}
