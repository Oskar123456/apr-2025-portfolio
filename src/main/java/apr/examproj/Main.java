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

import apr.examproj.geom.Bounds;
import apr.examproj.geom.Node;
import apr.examproj.geom.Way;
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
        Document doc = Jsoup.parse(osmStr, Parser.xmlParser());

        var boundsElmt = doc.getElementsByTag("bounds").get(0);
        System.out.printf("Main.loadOSM()::%n\tBounds: ");
        for (var bound : boundsElmt.getAllElements()) {
            for (var attr : bound.attributes()) {
                System.out.printf("(%s : %s)  ", attr.getKey(), attr.getValue());
            }
        }
        System.out.println();

        Bounds bounds = new Bounds(Double.valueOf(boundsElmt.attributes().get("minlat")),
                Double.valueOf(boundsElmt.attributes().get("maxlat")),
                Double.valueOf(boundsElmt.attributes().get("minlon")),
                Double.valueOf(boundsElmt.attributes().get("maxlon")));

        Map<String, Node> nodes = new HashMap<>();
        for (var node : doc.getElementsByTag("node")) {
            String id = node.attributes().get("id");
            double lat = Double.valueOf(node.attributes().get("lat"));
            double lon = Double.valueOf(node.attributes().get("lon"));
            nodes.put(id, new Node(id, lat, lon));
        }

        for (var e : doc.getElementsByTag("way")) {

            boolean isStreet = false;
            var tags = e.getElementsByTag("tag");
            for (var tag : tags) {
                if (tag.attributes().get("k").equals("highway")) {
                    isStreet = true;
                    break;
                }
            }

            if (!isStreet) {
                continue;
            }

            Way way = new Way();
            way.id = e.attributes().get("id");

            for (var tag : tags) {
                if (tag.attributes().get("k").equals("highway")) {
                    way.type = tag.attributes().get("v");
                }
                if (tag.attributes().get("k").equals("name")) {
                    way.name = tag.attributes().get("v");
                }
                if (tag.attributes().get("k").equals("maxspeed")) {
                    way.maxSpeed = Double.valueOf(tag.attributes().get("v"));
                }
            }

            var wayNodes = e.getElementsByTag("nd");
            for (var nd : wayNodes) {
                way.addNode(nodes.get(nd.attributes().get("ref")));
            }

            System.out.println(way.toString());
        }
    }

}
