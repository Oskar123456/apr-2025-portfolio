package apr;

import java.util.Random;

import apr.algorithms.AStar;
import apr.algorithms.Dijkstra;
import apr.datastructures.Graph;
import apr.datastructures.Node;
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
        var dijkstraResults = Dijkstra.wDefaults(graph, src);

        System.out.println(dijkstraResults);
    }

    static void testAStar() {
        // Random rng = new Random();
        // int n = rng.nextInt(5, 15);
        //
        // Graph<String> graph = new Graph<>();
        // graph.populate(n, () -> new Node<String>(String.format("N%02d",
        // SeqInt.next())));
        // graph.nodes.add(new Node<String>(String.format("N%02d", SeqInt.next())));
        // System.out.println(graph.toString());
        //
        // var src = graph.nodes.get(rng.nextInt(0, graph.nodes.size()));
        // var dest = graph.nodes.get(rng.nextInt(0, graph.nodes.size()));
        // var astarResults = AStar.use(graph, src, dest);
        //
        // System.out.println(dijkstraResults);
    }
}

class SeqInt {
    static int i;

    public static int next() {
        return i++;
    }
}
