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

        // final Canvas canvas = new Canvas(W, H);
        // GraphicsContext gc = canvas.getGraphicsContext2D();

        // root.getChildren().add(canvas);

        // AStarGUI.runMazes(gc, "data");

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

        // Text txt = new Text();
        // txt.setText("LOL");
        //
        // VBox sidePanel = new VBox();
        // HBox baseLayout = new HBox(20);
        //
        // sidePanel.getChildren().add(txt);
        // baseLayout.getChildren().add(sidePanel);
        // baseLayout.getChildren().add(guiExample);
        //
        // guiExample.setBorder(new Border(new BorderStroke(Color.BLACK,
        // BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        // guiExample.setPadding(new Insets(10));

        // baseLayout.setMinWidth(600);
        // baseLayout.setMinHeight(600);

        // HBox.setHgrow(sidePanel, Priority.ALWAYS);
        // HBox.setHgrow(guiExample, Priority.ALWAYS);
        // VBox.setVgrow(guiExample, Priority.ALWAYS);

        root.getChildren().add(hb);

        guiExample.start();

        stage.setScene(s);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}
