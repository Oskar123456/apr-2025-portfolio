package apr;

import java.io.IOException;
import java.util.List;

import apr.algorithms.graph.visualization.AStarGuiExample;
import apr.sorting.visualization.SortingGUIExample;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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

    static Text titleTxt;
    static Text footnoteTxt;
    static VBox sidePanel;
    static HBox topPanel, bottomPanel;
    static GUIExample content;

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
    public void start(Stage stage) {
        int W = 1000, H = 1000;
        double padding = 10;
        double topPanelH = 100;
        double bottomPanelH = 100;
        double sidePanelW = 100;

        root = new Group();
        Scene s = new Scene(root, W, H, Color.WHITE);
        s.getStylesheets().add("styles/AppGui.css");

        AnchorPane AP = new AnchorPane();
        // content = new AStarGuiExample(W - 2 * padding, H - 2 * padding);
        content = new SortingGUIExample();
        content.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        AP.getChildren().setAll(content);
        AnchorPane.setTopAnchor(content, 0D);
        AnchorPane.setBottomAnchor(content, 0D);
        AnchorPane.setLeftAnchor(content, 0D);
        AnchorPane.setRightAnchor(content, 0D);

        stage.widthProperty()
                .addListener((e, o, n) -> content.setPrefWidth(n.doubleValue() - sidePanelW - 2 * padding));
        stage.heightProperty().addListener(
                (e, o, n) -> content.setPrefHeight(n.doubleValue() - topPanelH - bottomPanelH - 2 * padding));

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

        layout.setTop(topPanel);
        layout.setBottom(bottomPanel);
        layout.setCenter(AP);
        layout.setLeft(sidePanel);

        AP.setId("Content");
        topPanel.setId("TopPanel");
        bottomPanel.setId("BottomPanel");
        sidePanel.setId("SidePanel");

        root.getChildren().add(layout);

        content.start();

        stage.setScene(s);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}
