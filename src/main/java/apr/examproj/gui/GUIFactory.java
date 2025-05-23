package apr.examproj.gui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

/**
 * GuiFactory
 */
public class GUIFactory {

    public static HBox defaultHBox(String id) {
        HBox pane = new HBox();
        pane.setId(id);
        return pane;
    }

    public static HBox defaultChildHBox(Pane parentPane, String id) {
        HBox pane = new HBox();
        pane.setId(id);
        pane.prefHeightProperty().bind(parentPane.heightProperty());
        pane.prefWidthProperty().bind(parentPane.widthProperty());
        parentPane.getChildren().add(pane);
        return pane;
    }

    public static VBox defaultChildVBox(Pane parentPane, String id) {
        VBox pane = new VBox();
        pane.setId(id);
        pane.prefHeightProperty().bind(parentPane.heightProperty());
        pane.prefWidthProperty().bind(parentPane.widthProperty());
        parentPane.getChildren().add(pane);
        return pane;
    }

    public static Pane defaultChildPane(Pane parentPane, String id) {
        Pane pane = new Pane();
        pane.setId(id);
        pane.prefHeightProperty().bind(parentPane.heightProperty());
        pane.prefWidthProperty().bind(parentPane.widthProperty());
        parentPane.getChildren().add(pane);
        return pane;
    }

    public static Pane defaultPane(String id) {
        Pane pane = new Pane();
        pane.setId(id);
        return pane;
    }

    public static BorderPane defaultChildBorderPane(Scene scene, String id) {
        BorderPane pane = new BorderPane();
        pane.setId(id);
        pane.prefHeightProperty().bind(scene.heightProperty());
        pane.prefWidthProperty().bind(scene.widthProperty());
        return pane;
    }

    public static Text defaultChildText(Pane parentPane, String id) {
        Text pane = new Text();
        pane.setId(id);
        // pane.prefHeightProperty().bind(parentPane.heightProperty());
        // pane.prefWidthProperty().bind(parentPane.widthProperty());
        parentPane.getChildren().add(pane);
        return pane;
    }

    // public static Polygon defaultChildPolygon(Pane parentPane, String id,
    // double... points) {
    // Polygon poly = new Polygon();
    // poly.setId(id);
    // poly.prefHeightProperty().bind(parentPane.heightProperty());
    // poly.prefWidthProperty().bind(parentPane.widthProperty());
    // return poly;
    // }

}
