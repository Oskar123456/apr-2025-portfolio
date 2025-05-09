package apr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import apr.algorithms.graph.visualization.AStarGuiExample;
import apr.sorting.BubbleSort;
import apr.sorting.SortFn;
import apr.sorting.visualization.SortingGUIExample;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * AppGui
 */
public class AppGui extends Application {

    static Group root;

    static List<GUIExample> guiExamples;
    static int curGuiExampleIdx = 0;

    static Scene scene;
    static BorderPane layout;
    static Text titleTxt;
    static Text footnoteTxt;
    static VBox sidePanel;
    static HBox topPanel, bottomPanel;
    static Pane content;

    static int W = 1200, H = 1000;
    static double padding = 4;
    static double topPanelH = 100;
    static double bottomPanelH = 100;
    static double sidePanelW = 150;
    static Insets margins = new Insets(padding);

    public void start(Stage stage) {
        root = new Group();
        scene = new Scene(root, W, H, Color.WHITE);
        scene.getStylesheets().add("styles/AppGui.css");

        stage.titleProperty().set("APR 2025 GUI EXAMPLES");

        layout = new BorderPane();
        layout.prefHeightProperty().bind(scene.heightProperty());
        layout.prefWidthProperty().bind(scene.widthProperty());

        setBottomPanel();
        setContent(stage);
        setTopPanel();
        setSidePanel(stage);

        layout.setTop(topPanel);
        layout.setBottom(bottomPanel);
        layout.setCenter(content);
        layout.setLeft(sidePanel);

        BorderPane.setMargin(topPanel, margins);
        BorderPane.setMargin(sidePanel, margins);
        BorderPane.setMargin(content, margins);
        BorderPane.setMargin(bottomPanel, margins);

        root.getChildren().add(layout);

        stage.setScene(scene);
        stage.show();
    }

    static void setGuiExample(int idx) {
        guiExamples.get(curGuiExampleIdx).stop();
        curGuiExampleIdx = idx;
        content.getChildren().setAll(guiExamples.get(curGuiExampleIdx));
        bottomPanel.getChildren().setAll(guiExamples.get(curGuiExampleIdx).options());
        guiExamples.get(curGuiExampleIdx).start();
    }

    static void setTopPanel() {
        topPanel = new HBox();
        topPanel.setId("top-panel");

        titleTxt = new Text("APR 2025 PORTFOLIO GUI EXAMPLES");
        titleTxt.setId("title");

        topPanel.getChildren().add(titleTxt);
        topPanel.setPrefHeight(topPanelH);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static void setSidePanel(Stage stage) {
        sidePanel = new VBox();
        sidePanel.setId("side-panel");

        sidePanel.setPrefWidth(sidePanelW);
        Text sidePanelTitle = new Text("Choose Demo:");
        sidePanel.getChildren().add(sidePanelTitle);
        sidePanelTitle.setId("side-panel__title");

        content.prefHeightProperty().bind(layout.heightProperty()
                .subtract(topPanelH + bottomPanelH + 6 * margins.getBottom()));

        ChoiceBox contentSelection = new ChoiceBox<>();
        contentSelection.setItems(FXCollections.observableArrayList(
                "Bubble Sort",
                "Bubble Sort X 2",
                new Separator(),
                "A* Algorithm"));
        contentSelection.getSelectionModel().clearAndSelect(0);
        setGuiExample(0);

        contentSelection.getSelectionModel().selectedIndexProperty().addListener((e, o, n) -> {
            setGuiExample(n.intValue());
        });

        sidePanel.getChildren().add(contentSelection);

    }

    static void setBottomPanel() {
        bottomPanel = new HBox();
        bottomPanel.setId("bottom-panel");
        bottomPanel.spacingProperty().set(20);
        bottomPanel.paddingProperty().set(new Insets(25));

        footnoteTxt = new Text("FOOTNOTES / INFO");
        footnoteTxt.setId("footnote");

        bottomPanel.getChildren().add(footnoteTxt);
        bottomPanel.setPrefHeight(bottomPanelH);
    }

    @SuppressWarnings("unchecked")
    static void setContent(Stage stage) {
        content = new Pane();
        content.setId("content");

        content.prefWidthProperty().bind(layout.widthProperty()
                .subtract(sidePanelW + 4 * margins.getLeft()));
        content.prefHeightProperty().bind(layout.heightProperty()
                .subtract(topPanelH + bottomPanelH + 6 * margins.getBottom()));

        System.out.printf("m: %f toppanelH: %f bottompanelH: %f%n", margins.getBottom(), topPanelH, bottomPanelH);

        guiExamples = new ArrayList<>();
        guiExamples.add(new SortingGUIExample("Bubble Sort",
                new SortFn[] { BubbleSort::sort },
                new String[] { "Bubble Sort" }));
        guiExamples.add(new SortingGUIExample("Bubble Sort",
                new SortFn[] { BubbleSort::sort, BubbleSort::sort },
                new String[] { "Bubble Sort", "Bubble Sort" }));
        guiExamples.add(null);
        guiExamples.add(new AStarGuiExample(W - 2 * padding, H - 2 * padding));

        for (var example : guiExamples) {
            if (example == null) {
                continue;
            }
            example.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            example.prefWidthProperty().bind(content.widthProperty());
            example.prefHeightProperty().bind(content.heightProperty());
        }

    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}
