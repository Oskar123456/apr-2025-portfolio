package apr.examproj.ds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Graph
 */
public class Graph<T> {

    GraphNode<T> src, dest;
    Set<GraphNode<T>> nodes = new HashSet<>();
    Set<GraphEdge<T>> edges = new HashSet<>();
    Map<GraphNode<T>, List<GraphNode<T>>> neighbors = new HashMap<>();
    Map<GraphNode<T>, List<GraphEdge<T>>> outgoingEdges = new HashMap<>();
    Map<GraphNode<T>, Double> dists = new HashMap<>();
    Map<GraphNode<T>, GraphNode<T>> srcs = new HashMap<>();

    List<GraphNode<T>> visitOrder = new ArrayList<>();
    List<GraphNode<T>> seenOrder = new ArrayList<>();

    int size;

    public Graph() {
    }

    public int size() {
        return size;
    }

    public List<GraphNode<T>> getPath() throws Exception {
        if (src == null || dest == null) {
            throw new Exception("no src or dest in graph");
        }
        if (dists.get(dest).equals(Double.POSITIVE_INFINITY)) {
            throw new Exception("no path from src to dest in graph");
        }

        List<GraphNode<T>> path = new ArrayList<>();
        GraphNode<T> curNode = dest;
        while (curNode != null) {
            path.add(curNode);
            curNode = srcs.get(curNode);
        }

        if (!path.contains(src)) {
            throw new Exception("no path from src to dest in graph");
        }

        return path.reversed();
    }

    public List<GraphNode<T>> getVisitOrder() {
        return visitOrder;
    }

    public List<GraphNode<T>> getSeenOrder() {
        return seenOrder;
    }

    public void setDestination(GraphNode<T> node) {
        if (this.src == node) {
            this.src = null;
        }
        this.dest = node;
    }

    public void setStart(GraphNode<T> node) {
        if (this.src != null) {
            dists.put(this.src, Double.POSITIVE_INFINITY);
        }
        if (this.dest == node) {
            this.dest = null;
        }
        this.src = node;
        dists.put(node, 0D);
    }

    public GraphNode<T> getSrc(GraphNode<T> dest) {
        return srcs.get(dest);
    }

    public void updateSrcs(GraphNode<T> src, GraphNode<T> dest) {
        srcs.put(dest, src);
    }

    public void updateDist(GraphNode<T> node, Double dist) {
        dists.put(node, dist);
    }

    public Double dist(GraphNode<T> node) {
        return dists.get(node);
    }

    /**
     * minimum weight of edges going from scr to dest
     */
    public Double dist(GraphNode<T> src, GraphNode<T> dest) {
        Double min = Double.POSITIVE_INFINITY;
        for (var edge : outgoingEdges.get(src)) {
            if (edge.dest == dest && edge.weight < min) {
                min = edge.weight;
            }
        }
        return min;
    }

    public List<GraphNode<T>> neighbors(GraphNode<T> node) {
        return neighbors.get(node);
    }

    public List<GraphEdge<T>> edgesFrom(GraphNode<T> node) {
        return outgoingEdges.get(node);
    }

    public void markAsVisited(GraphNode<T> node) {
        visitOrder.add(node);
    }

    public void markAsSeen(GraphNode<T> node) {
        seenOrder.add(node);
    }

    public void addNode(GraphNode<T> node) {
        nodes.add(node);
        if (!neighbors.containsKey(node)) {
            neighbors.put(node, new ArrayList<>());
        }
        dists.put(node, Double.POSITIVE_INFINITY);
        size++;
    }

    public void addEdge(GraphEdge<T> edge) {
        addNode(edge.src);
        addNode(edge.dest);
        edges.add(edge);
        neighbors.get(edge.src).add(edge.dest);
        outgoingEdges.get(edge.src).add(edge);
    }

}
