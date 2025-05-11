package apr.algorithms.graph.visualization;

import java.util.ArrayList;
import java.util.List;

import apr.GUIExample;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
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
    Text title;
    String titleStr;
    List<GraphAlg<Integer>> searchAlgs;
    GraphAlg<Integer> searchAlg;

    public GraphGUIExample() {
        options = new ArrayList<>();
        searchAlgs = new ArrayList<>();

        searchAlgs.add((g) -> GraphSolver.astar(g));
        searchAlgs.add(null);
        searchAlgs.add((g) -> GraphSolver.dijkstra(g));

        setupLayout();
        getChildren().setAll(layout);
        addOptions();

        animator = new GraphAnimator(content, "A*");
    }

    public void start() {
        animator.setAlg(searchAlg);
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    void addOptions() {
        Button optRunButton = new Button("Run");
        optRunButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            start();
        });
        options.add(optRunButton);

        Button optResetButton = new Button("Reset");
        optResetButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            animator.reset();
        });
        options.add(optResetButton);

        ChoiceBox optAlgs = new ChoiceBox<>();
        optAlgs.setItems(FXCollections.observableArrayList(
                "A*",
                new Separator(),
                "Dijkstra"));
        optAlgs.getSelectionModel().clearAndSelect(0);
        searchAlg = searchAlgs.get(0);

        optAlgs.getSelectionModel().selectedIndexProperty().addListener((e, o, n) -> {
            searchAlg = (searchAlgs.get(n.intValue()));
            title.setText(String.format(titleStr, optAlgs.getItems().get(n.intValue()).toString()));
        });
        options.add(optAlgs);
    }

    void setupLayout() {
        layout = new VBox();

        topPanel = new HBox();
        topPanel.setId("graph__title-panel");

        titleStr = "Graph GUI Example: %s";
        title = new Text(String.format(titleStr, "A*"));
        topPanel.getChildren().add(title);

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
