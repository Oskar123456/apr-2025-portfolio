package apr;

import apr.datastructure.Graph;
import apr.datastructure.Node;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        var label = new Label("Hello, JavaFX " + javafxVersion + ", running on Java " + javaVersion + ".");
        var scene = new Scene(new StackPane(label), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // launch();

        Graph<String> graph = new Graph<>();
        graph.populate(10, () -> new Node<String>(String.format("N%2d", SeqInt.next())));
        System.out.println(graph.toString());
    }
}

class SeqInt {
    static int i;

    public static int next() {
        return i++;
    }
}
