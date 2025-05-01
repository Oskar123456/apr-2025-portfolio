package apr.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * AStarGraph
 */
public class AStarGraph<T> {

    public List<AStarNode<T>> nodes;
    public List<AStarEdge<T>> edges;

    public AStarGraph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public String toString() {
        String str = String.format("Graph(nodes: %d, edges: %d)", nodes.size(), edges.size());

        str += String.format("%nNodes:");
        for (var n : nodes) {
            str += String.format("%n\t%s", n.toString());
        }
        str += String.format("%nEdges:");
        for (var e : edges) {
            str += String.format("%n\t%s", e.toString());
        }

        return str;
    }
}
