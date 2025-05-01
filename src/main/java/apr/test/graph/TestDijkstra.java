package apr.test.graph;

import java.util.Random;

import apr.algorithms.graph.Dijkstra;
import apr.datastructures.graph.Graph;
import apr.datastructures.graph.Node;

/**
 * TestDijkstra
 */
public class TestDijkstra {

    public static void testDijkstra() {
        Random rng = new Random();
        int n = rng.nextInt(5, 15);

        Graph<String> graph = new Graph<>();
        graph.populate(n, () -> new Node<String>(String.format("N%02d", SeqInt.next())));
        graph.nodes.add(new Node<String>(String.format("N%02d", SeqInt.next())));
        System.out.println(graph.toString());

        var src = graph.nodes.get(rng.nextInt(0, graph.nodes.size()));
        var dijkstraResults = Dijkstra.wDefaults(graph, src);

        System.out.println(dijkstraResults);
    }

    class SeqInt {
        static int i;

        public static int next() {
            return i++;
        }
    }
}
