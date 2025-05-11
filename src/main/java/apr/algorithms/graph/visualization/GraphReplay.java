package apr.algorithms.graph.visualization;

import java.util.ArrayList;
import java.util.List;

/**
 * GraphReplay
 */
public class GraphReplay<T> {

    List<Graph<T>> graphStates;
    int curIdx = -1;

    public GraphReplay(Graph<T> graph) {
        graphStates = new ArrayList<>();
        graph.visited.clear();
        graph.srcs.clear();
        graph.path.clear();

        for (var node : graph.nodes) {
            graph.dists.put(node, Double.POSITIVE_INFINITY);
        }
        graph.dists.put(graph.src, 0D);

        storeState(graph);
    }

    public void seal(Graph<T> graph) {
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
        storeState(graph);
    }

    public Graph<T> next() {
        return graphStates.get(++curIdx);
    }

    public boolean hasNext() {
        return curIdx + 1 < graphStates.size();
    }

    public void reset() {
        curIdx = -1;
    }

    public int getCurrentIndex() {
        return curIdx;
    }

    public void storeState(Graph<T> graph) {
        Graph<T> newGraph = new Graph<>();
        for (var node : graph.nodes)
            newGraph.addNode(node);
        for (var edge : graph.edges)
            newGraph.addEdge(edge);
        newGraph.dists.putAll(graph.dists);
        newGraph.srcs.putAll(graph.srcs);
        newGraph.visited.addAll(graph.visited);
        newGraph.path.addAll(graph.path);
        newGraph.src = graph.src;
        newGraph.dest = graph.dest;
        graphStates.add(newGraph);
    }
}
