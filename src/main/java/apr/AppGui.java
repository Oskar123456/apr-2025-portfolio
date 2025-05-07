package apr;

import java.io.IOException;
import java.util.List;

import apr.algorithms.graph.visualization.AStarGuiExample;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * AppGui
 */
public class AppGui extends Application {

    static Group root;
    static List<GUIExample> guiExamples;
    static GUIExample guiExample;

    @Override
    public void start(Stage stage) {
        // var javaVersion = SystemInfo.javaVersion();
        // var javafxVersion = SystemInfo.javafxVersion();

        int W = 800, H = 800;

        root = new Group();
        Scene s = new Scene(root, W, H, Color.WHITE);

        HBox hb = new HBox();
        VBox vb = new VBox();

        double padding = 10;
        hb.setAlignment(Pos.CENTER);
        hb.setPadding(new Insets(padding));
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(padding));

        guiExample = new AStarGuiExample(W - 2 * padding, H - 2 * padding);
        vb.getChildren().add(guiExample);
        hb.getChildren().add(vb);

        root.getChildren().add(hb);

        guiExample.start();

        stage.setScene(s);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}
