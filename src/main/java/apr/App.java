package apr;

import java.util.Random;

import apr.algorithms.AStar;
import apr.algorithms.Dijkstra;
import apr.datastructures.AStarEdge;
import apr.datastructures.AStarGraph;
import apr.datastructures.AStarNode;
import apr.datastructures.Graph;
import apr.datastructures.Node;
import apr.datastructures.Point2DI;
import apr.datastructures.TwoTuple;
// import javafx.application.Application;
// import javafx.scene.Scene;
// import javafx.scene.control.Label;
// import javafx.scene.layout.StackPane;
// import javafx.stage.Stage;

/**
 * JavaFX App
 */
// public class App extends Application {
public class App {

    // @SuppressWarnings("exports")
    // @Override
    // public void start(Stage stage) {
    // var javaVersion = SystemInfo.javaVersion();
    // var javafxVersion = SystemInfo.javafxVersion();
    //
    // var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java "
    // + javaVersion + ".");
    // var scene = new Scene(new StackPane(label), 640, 480);
    // stage.setScene(scene);
    // stage.show();
    // }

    public static void main(String[] args) {
        // launch();

        // testDijkstra();
        testAStar();
    }

    static void testAStar() {
        Random rng = new Random();
        int n = rng.nextInt(2, 10);

        AStarGraph<String> graph = new AStarGraph<>();

        for (int i = 0; i < n; i++) {
            graph.nodes.add(new AStarNode<>("N" + i, rng.nextInt(1, 10), rng.nextInt(1, 10)));
        }

        for (int i = 0; i < n; i++) {
            int srcId = i;
            int destId = rng.nextInt(0, n);
            if (destId == srcId) {
                destId = (destId + 1) % n;
            }
            var srcNode = graph.nodes.get(srcId);
            var destNode = graph.nodes.get(destId);

            int j = 0;
            while (j++ < n) {
                if (srcNode != destNode && !srcNode.isConnectedTo(destNode)) {
                    break;
                }
                destId = (destId + 1) % n;
                destNode = graph.nodes.get(destId);
            }

            if (srcNode == destNode || srcNode.isConnectedTo(destNode)) {
                continue;
            }

            var edge = new AStarEdge<>(srcNode, destNode);
            graph.edges.add(edge);
            srcNode.edges.add(edge);
        }

        System.out.println(graph);

        int srcId = rng.nextInt(0, n);
        int destId = rng.nextInt(0, n);
        if (destId == srcId) {
            destId = (destId + 1) % n;
        }

        AStar.use(graph, graph.nodes.get(srcId), graph.nodes.get(destId));

        /* GRID TESTING */
        int w = rng.nextInt(5, 10);
        int h = rng.nextInt(5, 10);

        int srcX = rng.nextInt(0, w);
        int srcY = rng.nextInt(0, h);
        int destX = rng.nextInt(0, w);
        int destY = rng.nextInt(0, h);

        if (srcX == destX && srcY == destY) {
            destX = (destX + 1) % w;
        }

        int[][] grid = new int[h][w];
        AStar.grid(grid, new Point2DI(srcX, srcY), new Point2DI(destX, destY));
    }

    static void testDijkstra() {
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
}

class SeqInt {
    static int i;

    public static int next() {
        return i++;
    }
}
