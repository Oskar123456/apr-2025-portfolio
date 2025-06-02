package apr.examproj.gui;

import apr.examproj.config.ApplicationConfig;
import apr.examproj.map.MapBounds;
import apr.examproj.map.MapNode;
import apr.examproj.utils.Geometry;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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

    public static Node defaultMapNode(MapNode node) {
        Pane pane = new Pane();
        double r = ApplicationConfig.mapNodeRadius;
        Ellipse dot = new Ellipse(r, r);
        dot.setId("street-map__default-node");
        Geometry.relocateToScreenCoords(pane, node.getPos());
        pane.getChildren().add(dot);
        return pane;
    }

    public static Node visitedMapNode(MapNode node) {
        Pane pane = new Pane();
        double r = ApplicationConfig.mapNodeRadius * 1.05;
        Ellipse dot = new Ellipse(r, r);
        dot.setId("street-map__visited-node");
        Geometry.relocateToScreenCoords(pane, node.getPos());
        pane.getChildren().add(dot);
        return pane;
    }

    public static Node highlightedMapNode(MapNode node) {
        Pane pane = new Pane();
        double r = ApplicationConfig.mapNodeRadius * 1.25;
        Ellipse dot = new Ellipse(r, r);
        dot.setId("street-map__highlighted-node");
        Geometry.relocateToScreenCoords(pane, node.getPos());
        pane.getChildren().add(dot);
        return pane;
    }

    public static Node defaultMapNode(Pane renderPane, MapBounds bounds, MapNode node) {
        Pane pane = new Pane();
        double radius = ApplicationConfig.mapNodeRadius
                * Math.min(renderPane.getWidth(), renderPane.getHeight());
        Ellipse dot = new Ellipse(radius, radius);
        dot.setId("street-map__default-node");
        var pos = GUIUtils.mapNodeCoordsToPane(bounds, renderPane, node);
        pane.relocate(pos.x, pos.y);
        pane.getChildren().add(dot);
        renderPane.getChildren().add(pane);
        return pane;
    }

    public static Node visitedMapNode(Pane renderPane, MapBounds bounds, MapNode node) {
        Pane pane = new Pane();
        double radius = ApplicationConfig.mapNodeRadius
                * Math.min(renderPane.getWidth(), renderPane.getHeight());
        Ellipse dot = new Ellipse(radius, radius);
        dot.setId("street-map__visited-node");
        var pos = GUIUtils.mapNodeCoordsToPane(bounds, renderPane, node);
        pane.relocate(pos.x, pos.y);
        pane.getChildren().add(dot);
        // renderPane.getChildren().add(pane);
        return pane;
    }

    public static Node highlightedMapNode(Pane renderPane, MapBounds bounds, MapNode node) {
        double radius = 1.2 * ApplicationConfig.mapNodeRadius
                * Math.min(renderPane.getWidth(), renderPane.getHeight());
        Ellipse dot = new Ellipse(radius, radius);
        dot.setId("street-map__highlight-node");
        var pos = GUIUtils.mapNodeCoordsToPane(bounds, renderPane, node);
        dot.relocate(pos.x, pos.y);
        // renderPane.getChildren().add(dot);
        return dot;
    }

}
