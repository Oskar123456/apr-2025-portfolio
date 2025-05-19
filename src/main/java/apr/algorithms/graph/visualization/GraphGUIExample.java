package apr.algorithms.graph.visualization;

import java.util.ArrayList;
import java.util.List;

import apr.GUIExample;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
    boolean paused;

    public GraphGUIExample() {
        options = new ArrayList<>();
        searchAlgs = new ArrayList<>();

        searchAlgs.add((g) -> GraphSolver.astar(g));
        searchAlgs.add(null);
        searchAlgs.add((g) -> GraphSolver.dijkstra(g));

        setupLayout();
        getChildren().setAll(layout);

        animator = new GraphAnimator(content, "A*");
        addOptions();
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
        HBox hb = new HBox(10);
        VBox vb = new VBox();

        hb.alignmentProperty().set(Pos.CENTER);
        vb.alignmentProperty().set(Pos.CENTER);

        hb.getChildren().addAll(options);
        vb.getChildren().add(hb);

        return List.of(vb);
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

        Button optGenerateButton = new Button("Generate");
        optGenerateButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            animator.generate();
        });
        options.add(optGenerateButton);

        Button optPauseButton = new Button("Pause");
        optPauseButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            if (paused) {
                optPauseButton.setText("Pause");
                animator.unpause();
                paused = false;
            } else {
                optPauseButton.setText("Unpause");
                animator.unpause();
                paused = true;
            }
        });
        options.add(optPauseButton);

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

        HBox optSeen = new HBox(10);
        HBox optVis = new HBox(10);
        HBox optPath = new HBox(10);

        Rectangle optSeenR = new Rectangle(15, 15, animator.colorSeen);
        Rectangle optVisR = new Rectangle(15, 15, animator.colorVis);
        Rectangle optPathR = new Rectangle(15, 15, animator.colorPath);

        optSeen.getChildren().setAll(optSeenR, new Text("Seen"));
        optVis.getChildren().setAll(optVisR, new Text("Visited"));
        optPath.getChildren().setAll(optPathR, new Text("Optimal path"));

        options.add(optSeen);
        options.add(optVis);
        options.add(optPath);
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
