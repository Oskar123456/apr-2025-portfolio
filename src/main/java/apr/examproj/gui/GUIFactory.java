package apr.examproj.gui;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
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

    public static Text defaultChildText(Pane parentPane, String text, String id) {
        Text txt = new Text(text);
        txt.setId(id);
        txt.getStyleClass().add("text");
        parentPane.getChildren().add(txt);
        return txt;
    }

    public static Pane defaultCircleSign(String text, double radius) {
        double brdrRadius = 0.15;

        Pane container = new Pane();
        container.setPrefHeight(0);
        container.setPrefWidth(0);

        Ellipse outer = new Ellipse(radius, radius);
        Text txt = new Text(text);

        outer.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
            outer.setStroke(Color.RED);
            outer.fillProperty().set(Color.DARKSALMON);
        });
        outer.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> {
            outer.setStroke(Color.BLACK);
            outer.fillProperty().set(Color.ALICEBLUE);
        });
        txt.hoverProperty().subscribe((a, b) -> {
            if (a) {
                outer.setStroke(Color.BLACK);
                outer.fillProperty().set(Color.ALICEBLUE);
            }
            if (b) {
                outer.setStroke(Color.RED);
                outer.fillProperty().set(Color.DARKSALMON);
            }
        });

        container.setId("street-map__circle-sign");
        outer.setId("street-map__circle-sign-outer");
        txt.setId("street-map__circle-sign-text");

        container.getChildren().addAll(outer, txt);
        txt.relocate(-radius / 2, -radius / 2);
        return container;
    }

    public static Text defaultText(String text) {
        Text txt = new Text(text);
        txt.setId("street-map__text");
        txt.getStyleClass().add("text");
        return txt;
    }

}
