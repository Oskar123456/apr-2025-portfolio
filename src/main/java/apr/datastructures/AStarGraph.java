package apr.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * AStarGraph
 */
public class AStarGraph<T> {

    List<AStarNode<T>> nodes;
    List<AStarEdge<T>> edges;

    public AStarGraph() {
        nodes = new ArrayList<>();
        edges = new ArrayList<>();
    }
}
