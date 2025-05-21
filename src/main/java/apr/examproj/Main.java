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
        loadOSM("data/map.osm");
        // launch();
    }

    public void start(Stage stage) {
        root = new Group();
        scene = new Scene(root, W, H, Color.WHITE);
        scene.getStylesheets().add("styles/exam-project.css");

        layout = new BorderPane();
        layout.setId("exam-proj__layout");
        layout.prefHeightProperty().bind(scene.heightProperty());
        layout.prefWidthProperty().bind(scene.widthProperty());

        title = new HBox();
        title.setId("exam-proj__title");
        title.getChildren().add(new Text("APR 2025 EXAM PROJECT"));
        title.prefHeight(75);
        // title.prefWidthProperty().bind(layout.widthProperty()
        // .subtract(margins.getRight()).subtract(margins.getLeft()));

        content = new Pane();
        content.setId("exam-proj__content");
        content.prefHeightProperty().bind(layout.heightProperty()
                .subtract(title.heightProperty()));
        // content.prefWidthProperty().bind(layout.widthProperty()
        // .subtract(margins.getRight()).subtract(margins.getLeft()));

        layout.setTop(title);
        layout.setCenter(content);

        root.getChildren().add(layout);

        stage.titleProperty().set("APR 2025 EXAM PROJECT");
        stage.setScene(scene);
        stage.show();
    }

    static void loadOSM(String path) throws IOException {
        String osmStr = new String(Files.readAllBytes(Paths.get(path)));
        MapData mapData = new MapData(osmStr);
        StreetMap streetMap = new StreetMap(mapData);

        System.out.println(mapData);
        System.out.println(streetMap);
    }

}
