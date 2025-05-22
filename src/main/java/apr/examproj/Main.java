package apr.examproj;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import apr.examproj.ds.StreetMap;
import apr.examproj.geom.MapBounds;
import apr.examproj.geom.MapNode;
import apr.examproj.geom.MapWay;
import apr.examproj.gui.GuiFactory;
import apr.examproj.osm.MapData;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
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

        layout = GuiFactory.defaultChildBorderPane(scene, "exam-proj__layout");
        layout.prefWidthProperty().bind(scene.widthProperty());
        layout.prefHeightProperty().bind(scene.heightProperty());

        title = GuiFactory.defaultHBox("exam-proj__title");
        title.getChildren().add(new Text("APR 2025 EXAM PROJECT"));
        title.prefHeight(75);

        content = GuiFactory.defaultPane("exam-proj__content");
        content.prefWidthProperty().bind(layout.widthProperty());
        content.prefHeightProperty().bind(layout.heightProperty()
                .subtract(title.heightProperty()));

        layout.setTop(title);
        layout.setCenter(content);

        root.getChildren().add(layout);

        stage.titleProperty().set("APR 2025 EXAM PROJECT");
        stage.setScene(scene);
        stage.show();

        loadOSM("data/map.osm");
    }

    static void loadOSM(String path) throws IOException {
        String osmStr = new String(Files.readAllBytes(Paths.get(path)));
        MapData mapData = new MapData(osmStr);
        StreetMap streetMap = new StreetMap(mapData);
        streetMap.setRenderTarget(content);
    }

}
