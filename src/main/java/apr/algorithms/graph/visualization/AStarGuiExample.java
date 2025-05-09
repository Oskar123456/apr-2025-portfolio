package apr.algorithms.graph.visualization;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import apr.GUIExample;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
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

    double padding = 10;
    double sidePanelW = 125;
    double topPanelH = 50;

    boolean paused;

    Canvas canvas;
    Text sliderTxt;

    public AStarGuiExample(double width, double height) {

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

        sliderTxt = new Text("Duration (secs): 5");

        Slider sidePanelSlider = new Slider();
        sidePanelSlider.setId("AStar__side-panel-slider");
        sidePanelSlider.setValue(5);
        sidePanelSlider.setMin(1);
        sidePanelSlider.setMax(10);
        sidePanelSlider.setShowTickMarks(true);
        sidePanelSlider.setShowTickLabels(true);
        sidePanelSlider.setMajorTickUnit(1);
        sidePanelSlider.setBlockIncrement(1);
        sidePanel.getChildren().add(sliderTxt);
        sidePanel.getChildren().add(sidePanelSlider);

        Button sidePanelButton = new Button();
        sidePanelButton.setText("pause");
        sidePanelButton.setId("AStar__side-panel-button");
        sidePanel.getChildren().add(sidePanelButton);

        topPanel.setPrefHeight(topPanelH);
        sidePanel.setPrefWidth(sidePanelW);
        content.setPrefSize(width - sidePanelW - padding, height - topPanelH);

        canvas = new Canvas();
        canvas.widthProperty().bind(widthProperty().subtract(sidePanelW + padding));
        canvas.heightProperty().bind(heightProperty().subtract(topPanelH));

        layout.setTop(topPanel);
        layout.setLeft(sidePanel);
        layout.setCenter(canvas);
        getChildren().add(layout);

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
                // AStarMaze.solveAllowDiag(m, (a, b) -> Math.sqrt((a.x - b.x) * (a.x - b.x) +
                // (a.y - b.y) * (a.y - b.y)));
                AStarMaze.solveAllowDiag(m, (a, b) -> {
                    double dx = Math.abs(a.x - b.x);
                    double dy = Math.abs(a.y - b.y);
                    double max = Math.max(dx, dy);
                    double min = Math.min(dx, dy);
                    return Math.sqrt((min * min) + (min * min)) + (max - min);
                });
                solvedMazes.add(m);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        animator = new AStarAnimator(canvas.getGraphicsContext2D(), durationInSecs, solvedMazes);

        sidePanelSlider.valueProperty().addListener((e, o, n) -> adjustAnimDuration(e, o, n));

        sidePanelButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (paused) {
                sidePanelButton.setText("unpause");
                animator.start();
            } else {
                sidePanelButton.setText("pause");
                animator.stop();
            }
            paused = !paused;
        });

        canvas.widthProperty().addListener(e -> animator.draw());
        canvas.heightProperty().addListener(e -> animator.draw());
    }

    void adjustAnimDuration(ObservableValue<? extends Number> ch, Number o, Number n) {
        animator.duration = n.longValue() * 1000000000;
        sliderTxt.setText("Duration (secs):" + n.longValue());
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

    public List<Node> options() {
        return new ArrayList<>();
    }

}
