package apr.examproj;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import apr.examproj.application.StreetMapApp;
import apr.examproj.gui.GUIFactory;
import apr.examproj.gui.SelectionList;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Main
 */
public class Main extends Application {

    static Group root;
    static Scene scene;
    static BorderPane layout;
    static Pane content;
    static HBox title;
    Insets margins = new Insets(10);

    static int W = 800, H = 800;

    public static void main(String[] args) throws IOException {
        launch();
    }

    public void start(Stage stage) throws IOException {
        root = new Group();
        scene = new Scene(root, W, H, Color.WHITE);
        scene.getStylesheets().add("styles/exam-project.css");

        layout = GUIFactory.defaultChildBorderPane(scene, "exam-proj__layout");
        layout.prefWidthProperty().bind(scene.widthProperty());
        layout.prefHeightProperty().bind(scene.heightProperty());

        title = GUIFactory.defaultHBox("exam-proj__title");
        GUIFactory.defaultChildText(title, "APR 2025 EXAM PROJECT", "exam-proj__title-text");

        content = GUIFactory.defaultHBox("exam-proj__content");
        content.prefWidthProperty().bind(layout.widthProperty());
        content.prefHeightProperty().bind(layout.heightProperty()
                .subtract(title.heightProperty()));
        Rectangle clipRect = new Rectangle();
        clipRect.widthProperty().bind(content.widthProperty());
        clipRect.heightProperty().bind(content.heightProperty());
        content.setClip(clipRect);

        layout.setTop(title);
        layout.setCenter(content);

        root.getChildren().add(layout);

        stage.titleProperty().set("APR 2025 EXAM PROJECT");
        stage.setScene(scene);
        stage.show();

        SelectionList selList = new SelectionList();
        content.getChildren().add(selList);

        List<File> osmFiles = new ArrayList<>();
        File dataDir = new File("data");
        for (var f : dataDir.listFiles()) {
            if (f.isFile() && f.getName().endsWith("osm")) {
                osmFiles.add(f);
            }
        }
        for (var osm : osmFiles) {
            selList.addButton(e -> launchStreetMaps(osm.getAbsolutePath()),
                    String.format("%s (%.2fmb)", osm.getName(), osm.length() / 1000000D));
        }

    }

    void launchStreetMaps(String osmPath) {
        content.getChildren().clear();
        Pane subContent = new Pane();
        content.getChildren().add(subContent);
        try {
            StreetMapApp.start(osmPath, subContent);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

}
