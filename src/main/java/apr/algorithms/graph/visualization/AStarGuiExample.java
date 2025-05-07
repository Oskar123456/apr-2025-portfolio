package apr.algorithms.graph.visualization;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import apr.GUIExample;
import apr.algorithms.graph.visualization.AStarMaze;
import apr.algorithms.graph.visualization.Maze;
import javafx.scene.canvas.Canvas;

/**
 * AStarGuiExample
 */
public class AStarGuiExample extends GUIExample {

    AStarAnimator animator;

    public AStarGuiExample(double width, double height) {
        setMinWidth(width);
        setMinHeight(height);

        Canvas canvas = new Canvas(width, height);
        getChildren().add(canvas);

        int durationInSecs = 5;

        List<Maze> solvedMazes = new ArrayList<>();
        try {
            File dir = new File("data");
            for (var file : dir.listFiles()) {
                if (file.isDirectory() || !file.getName().startsWith("maze")) {
                    continue;
                }
                Maze m = Maze.fromText(Files.readAllLines(file.toPath()));
                // AStarMaze.solve(m, (a, b) -> Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y -
                // b.y) * (a.y - b.y)));
                AStarMaze.solveAllowDiag(m, (a, b) -> Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y)));
                solvedMazes.add(m);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        animator = new AStarAnimator(canvas.getGraphicsContext2D(), durationInSecs, solvedMazes);
    }

    public void start() {
        if (animator != null) {
            animator.start();
        } else {
            System.out.println("AStarGuiExample::start: animator is null");
        }
    }

    public void pause() {
        animator.stop();
    }

    public void stop() {
        animator.stop();
    }

}
