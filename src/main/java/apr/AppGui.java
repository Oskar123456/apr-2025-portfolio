package apr;

import java.io.IOException;
import java.util.List;

import apr.algorithms.graph.visualization.AStarGuiExample;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
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

    @Override
    public void start(Stage stage) {
        // var javaVersion = SystemInfo.javaVersion();
        // var javafxVersion = SystemInfo.javafxVersion();

        int W = 1000, H = 1000;
        double padding = 10;

        root = new Group();
        Scene s = new Scene(root, W, H, Color.WHITE);

        content = new AStarGuiExample(W - 2 * padding, H - 2 * padding);

        topPanel = new HBox();
        bottomPanel = new HBox();

        titleTxt = new Text("APR 2025 PORTFOLIO GUI EXAMPLES");
        titleTxt.setStyle("-fx-font-size: 20px");
        topPanel.getChildren().add(titleTxt);
        footnoteTxt = new Text("FOOTNOTES / INFO");
        footnoteTxt.setStyle("-fx-font-size: 10px");
        bottomPanel.getChildren().add(footnoteTxt);

        sidePanel = new VBox();
        sidePanel.setMinWidth(100);
        Text sidePanelTitle = new Text("Side Panel");
        sidePanelTitle.setStyle("-fx-font-size: 15px");
        sidePanel.getChildren().add(sidePanelTitle);

        BorderPane layout = new BorderPane();

        layout.setTop(topPanel);
        layout.setBottom(bottomPanel);
        layout.setCenter(content);
        layout.setLeft(sidePanel);

        root.getChildren().add(layout);

        content.start();

        stage.setScene(s);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
        launch();
    }
}
