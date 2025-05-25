package apr.examproj;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import apr.examproj.driver.StreetMapDriver;
import apr.examproj.gui.GUIFactory;
import apr.examproj.map.StreetMap;
import apr.examproj.osm.MapData;
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

    static int W = 1200, H = 1000;

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

        content = GUIFactory.defaultPane("exam-proj__content");
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

        StreetMapDriver driver = new StreetMapDriver();
        driver.start(content);
    }

    static void loadOSM(String path) throws IOException {
        String osmStr = new String(Files.readAllBytes(Paths.get(path)));
        MapData mapData = new MapData(osmStr);
        StreetMap streetMap = new StreetMap(mapData);
        System.out.println("Main.loadOSM()");
        System.out.println(streetMap);
        streetMap.setRenderTarget(content);
    }

}
