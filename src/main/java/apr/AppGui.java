package apr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import apr.algorithms.graph.visualization.AStarGuiExample;
import apr.sorting.BubbleSort;
import apr.sorting.visualization.SortingGUIExample;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.layout.AnchorPane;
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

    static Text titleTxt;
    static Text footnoteTxt;
    static VBox sidePanel;
    static HBox topPanel, bottomPanel;
    static Pane content;

    // @Override
    // public void start(Stage stage) {
    // // var javaVersion = SystemInfo.javaVersion();
    // // var javafxVersion = SystemInfo.javafxVersion();
    //
    // int W = 1000, H = 1000;
    // double padding = 10;
    //
    // root = new Group();
    // Scene s = new Scene(root, W, H, Color.WHITE);
    //
    // content = new AStarGuiExample(W - 2 * padding, H - 2 * padding);
    //
    // root.getChildren().add(content);
    //
    // content.start();
    //
    // stage.setScene(s);
    // stage.show();
    // }

    @Override
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void start(Stage stage) {
        int W = 1000, H = 1000;
        double padding = 10;
        double topPanelH = 100;
        double bottomPanelH = 100;
        double sidePanelW = 100;

        root = new Group();
        Scene s = new Scene(root, W, H, Color.WHITE);
        s.getStylesheets().add("styles/AppGui.css");

        topPanel = new HBox();
        bottomPanel = new HBox();

        titleTxt = new Text("APR 2025 PORTFOLIO GUI EXAMPLES");
        titleTxt.setId("Title");
        topPanel.getChildren().add(titleTxt);
        topPanel.setMinHeight(topPanelH);
        footnoteTxt = new Text("FOOTNOTES / INFO");
        footnoteTxt.setId("Footnote");
        bottomPanel.getChildren().add(footnoteTxt);
        bottomPanel.setMinHeight(bottomPanelH);
        bottomPanel.setId("BottomPanel");

        sidePanel = new VBox();
        sidePanel.setPrefWidth(sidePanelW);
        Text sidePanelTitle = new Text("Side Panel");
        sidePanel.getChildren().add(sidePanelTitle);

        BorderPane layout = new BorderPane();

        content = new Pane();
        content.prefWidthProperty().bind(stage.widthProperty().subtract(sidePanelW + 2 * padding));
        content.prefHeightProperty().bind(stage.heightProperty().subtract(topPanelH + bottomPanelH + 2 * padding));
        // stage.widthProperty()
        // .addListener((e, o, n) -> content.setPrefWidth(n.doubleValue() - sidePanelW -
        // 2 * padding));
        // stage.heightProperty().addListener(
        // (e, o, n) -> content.setPrefHeight(n.doubleValue() - topPanelH - bottomPanelH
        // - 2 * padding));

        layout.setTop(topPanel);
        layout.setBottom(bottomPanel);
        layout.setCenter(content);
        layout.setLeft(sidePanel);

        content.setId("Content");
        topPanel.setId("TopPanel");
        bottomPanel.setId("BottomPanel");
        sidePanel.setId("SidePanel");

        root.getChildren().add(layout);

        guiExamples = new ArrayList<>();
        guiExamples.add(new SortingGUIExample(BubbleSort::sort));
        guiExamples.add(null);
        guiExamples.add(new AStarGuiExample(W - 2 * padding, H - 2 * padding));

        for (var example : guiExamples) {
            if (example == null) {
                continue;
            }
            example.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            example.prefWidthProperty().bind(content.widthProperty().subtract(2 * padding));
            example.prefHeightProperty().bind(content.heightProperty().subtract(2 * padding));
        }

        ChoiceBox contentSelection = new ChoiceBox<>();
        contentSelection.setItems(FXCollections.observableArrayList("Bubble Sort", new Separator(), "A* Algorithm"));

        contentSelection.getSelectionModel().selectedIndexProperty().addListener((e, o, n) -> {
            guiExamples.get(curGuiExampleIdx).stop();
            curGuiExampleIdx = n.intValue();
            content.getChildren().setAll(guiExamples.get(curGuiExampleIdx));
            guiExamples.get(curGuiExampleIdx).start();
        });

        sidePanel.getChildren().add(contentSelection);

        stage.setScene(s);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}
