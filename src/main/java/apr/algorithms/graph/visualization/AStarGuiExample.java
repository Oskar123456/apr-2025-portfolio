package apr.algorithms.graph.visualization;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import apr.GUIExample;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * AStarGuiExample
 */
public class AStarGuiExample extends GUIExample {

    AStarAnimator animator;

    public AStarGuiExample(double width, double height) {
        double padding = 10;
        double sidePanelW = 50;
        double topPanelH = 50;

        BorderPane layout = new BorderPane();

        HBox topPanel = new HBox();
        topPanel.setId("AStar__top-panel");
        VBox sidePanel = new VBox();
        sidePanel.setId("AStar__side-panel");
        Pane content = new Pane();
        content.setId("AStar__content");

        Text titleTxt = new Text("A* Visualization");
        titleTxt.setId("AStar__title");
        topPanel.getChildren().add(titleTxt);

        Button sidePanelButton = new Button();
        sidePanelButton.setText("button");
        sidePanelButton.setId("AStar__side-panel-button");
        sidePanel.getChildren().add(sidePanelButton);

        topPanel.setPrefHeight(topPanelH);
        sidePanel.setPrefWidth(sidePanelW);
        content.setPrefSize(width - sidePanelW - padding, height - topPanelH);

        Canvas canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        canvas.widthProperty().addListener((e, o, n) -> canvas.prefWidth(n.doubleValue()));
        canvas.heightProperty().addListener((e, o, n) -> canvas.prefHeight(n.doubleValue()));
        canvas.widthProperty().addListener((e, o, n) -> canvas.resize(n.doubleValue() - padding, canvas.getHeight()));
        canvas.heightProperty().addListener((e, o, n) -> canvas.resize(canvas.getWidth(), n.doubleValue()));

        layout.setTop(topPanel);
        layout.setLeft(sidePanel);
        layout.setCenter(canvas);
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

        canvas.widthProperty().addListener(e -> animator.draw());
        canvas.heightProperty().addListener(e -> animator.draw());
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
