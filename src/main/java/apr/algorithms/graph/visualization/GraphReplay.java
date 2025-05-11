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
        newGraph.nodes.addAll(graph.nodes);
        newGraph.edges.addAll(graph.edges);
        newGraph.dists.putAll(graph.dists);
        newGraph.srcs.putAll(graph.srcs);
        newGraph.visited.addAll(graph.visited);
        newGraph.path.addAll(graph.path);
        newGraph.src = graph.src;
        newGraph.dest = graph.dest;
        graphStates.add(newGraph);
    }
}
