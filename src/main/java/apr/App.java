package apr;

import java.util.Random;

import apr.algorithms.Dijkstra;
import apr.datastructures.Graph;
import apr.datastructures.Node;
import apr.datastructures.TwoTuple;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
    // public class App {

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java "
                + javaVersion + ".");
        var scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // launch();

        testDijkstra();
    }

    static void testDijkstra() {
        Random rng = new Random();
        int n = rng.nextInt(5, 15);

        Graph<String> graph = new Graph<>();
        graph.populate(n, () -> new Node<String>(String.format("N%02d", SeqInt.next())));
        graph.nodes.add(new Node<String>(String.format("N%02d", SeqInt.next())));
        System.out.println(graph.toString());

        var src = graph.nodes.get(rng.nextInt(0, graph.nodes.size()));
        var dijkstraResults = Dijkstra.use(graph, src);
        var dists = dijkstraResults.first;
        var srcs = dijkstraResults.second;

        System.out.printf("%nDijkstra results (shortest path to %s):%n", src.toString());
        for (var kv : dists.entrySet()) {
            var node = kv.getKey();
            var dist = kv.getValue();
            String distStr = dist == Double.POSITIVE_INFINITY ? "INF" : String.format("%3.1f", dist);
            System.out.printf("\t%s (dist = %s)", node.toString(), distStr);
            if (dist != Double.POSITIVE_INFINITY) {
                System.out.printf(" PATH: ");
                var pathNode = node;
                while (true) {
                    System.out.printf("%s <-- ", pathNode.toString());
                    pathNode = srcs.get(pathNode);
                    if (pathNode == src) {
                        System.out.printf("%s", pathNode.toString());
                        break;
                    }
                }
            }
            System.out.println();
        }
    }
}

class SeqInt {
    static int i;

    public static int next() {
        return i++;
    }
}
