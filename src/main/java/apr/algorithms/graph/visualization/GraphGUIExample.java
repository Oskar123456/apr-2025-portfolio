package apr.algorithms.graph.visualization;

import java.util.ArrayList;
import java.util.List;

import apr.GUIExample;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * GraphGUIExample
 */
public class GraphGUIExample extends GUIExample {

    GraphAnimator animator;
    List<Node> options;
    Pane layout, topPanel, content;

    public GraphGUIExample() {
        options = new ArrayList<>();

        setupLayout();
        getChildren().setAll(layout);
        addOptions();

        animator = new GraphAnimator(content, "A*");
    }

    public void start() {
        animator.search("A*", (g) -> GraphSolver.astar(g, (a, b) -> a.pos.dist(b.pos)));
        animator.start();
    }

    public void pause() {
        animator.stop();
    }

    public void stop() {
        animator.stop();
    }

    public List<Node> options() {
        return options;
    }

    void addOptions() {
        Button optRunButton = new Button("Run");
        optRunButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            start();
        });
        options.add(optRunButton);
    }

    void setupLayout() {
        layout = new VBox();

        topPanel = new HBox();
        topPanel.setId("graph__title-panel");
        topPanel.getChildren().add(new Text("Graph GUI Example"));

        content = new HBox();
        content.setId("graph__content");

        layout.prefWidthProperty().bind(widthProperty());
        layout.prefHeightProperty().bind(heightProperty());

        topPanel.prefWidthProperty().bind(widthProperty());
        topPanel.prefHeightProperty().set(50);

        content.prefWidthProperty().bind(widthProperty());
        content.prefHeightProperty().bind(heightProperty().subtract(topPanel.heightProperty()));

        widthProperty().addListener((e) -> {
            animator.draw();
        });
        heightProperty().addListener((e) -> {
            animator.draw();
        });

        layout.getChildren().setAll(topPanel, content);
    }

}
