package apr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import apr.algorithms.graph.AStarGUI;
import apr.algorithms.graph.AStarGUI.Timmy;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * AppGui
 */
public class AppGui extends Application {

    @Override
    public void start(Stage stage) {
        var javaVersion = SystemInfo.javaVersion();
        var javafxVersion = SystemInfo.javafxVersion();

        int W = 800, H = 800;

        Group root = new Group();
        Scene s = new Scene(root, W, H, Color.WHITE);

        final Canvas canvas = new Canvas(W, H);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        AStarGUI.Maze maze;
        try {
            File dir = new File("data");

            Timmy timer = new Timmy();
            timer.mazesList = new ArrayList<>();
            timer.gc = gc;

            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    continue;
                }
                if (!file.getName().startsWith("maze")) {
                    continue;
                }
                maze = AStarGUI.readMaze(file.getAbsolutePath());
                List<AStarGUI.Maze> mazes = AStarGUI.solve(maze);
                timer.mazesList.add(mazes);

                System.out.printf("added %s%n", file.getAbsolutePath());
            }

            timer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

        stage.setScene(s);
        stage.show();

        // draw(gc);
    }

    public static void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE);

        gc.fillRect(75, 75, 100, 100);
    }

    public static void main(String[] args) throws IOException {

        launch();
    }
}
