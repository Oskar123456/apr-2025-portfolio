package apr.examproj.gui;

import java.util.ArrayList;
import java.util.List;

import apr.datastructures.graph.Point2D;
import apr.examproj.application.StreetMapApp;
import apr.examproj.config.ApplicationConfig;
import apr.examproj.map.MapAddress;
import apr.examproj.map.MapBounds;
import apr.examproj.map.MapBuilding;
import apr.examproj.map.MapNode;
import apr.examproj.map.MapPath;
import apr.examproj.utils.Geometry;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
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

    public static Pane highlightedMapNode(MapNode node) {
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
        return pane;
    }

    public static Node highlightedMapNode(Pane renderPane, MapBounds bounds, MapNode node) {
        double radius = 1.2 * ApplicationConfig.mapNodeRadius
                * Math.min(renderPane.getWidth(), renderPane.getHeight());
        Ellipse dot = new Ellipse(radius, radius);
        dot.setId("street-map__highlight-node");
        var pos = GUIUtils.mapNodeCoordsToPane(bounds, renderPane, node);
        dot.relocate(pos.x, pos.y);
        return dot;
    }

    public static Polyline pathLine(MapPath path) {
        Polyline line = new Polyline();
        line.setId("street-map__path");

        List<Point2D> pointsSeen = new ArrayList<>();
        List<Double> coords = new ArrayList<>();
        path.getNodes().forEach(n -> {
            var p = Geometry.toScreenCoords(Geometry.normalize(n.lat, n.lon));

            if (Double.isNaN(p.x) || Double.isNaN(p.y)) {
                return;
            }

            for (var pp : pointsSeen) {
                if ((Math.abs(p.x - pp.x)) <= 5 && (Math.abs(p.y - pp.y)) <= 5) {
                    return;
                }
            }
            pointsSeen.add(p);

            coords.add(p.x);
            coords.add(p.y);
        });
        line.getPoints().addAll(coords);

        Tooltip.setTooltip(line, path.toString(), path.getDescription(), "id: " + path.id);

        return line;
    }

    public static Polygon buildingPolygon(MapBuilding building) {
        Polygon polygon = new Polygon();
        polygon.setId("street-map__building");

        List<Double> coords = new ArrayList<>();
        building.getNodes().forEach(n -> {
            var p = Geometry.toScreenCoords(Geometry.normalize(n.lat, n.lon));
            coords.add(p.x);
            coords.add(p.y);
        });
        polygon.getPoints().addAll(coords);

        return polygon;
    }

    public static Polyline linkPath(MapPath path) {
        Polyline line = new Polyline();
        line.setId("street-map__link-path");

        List<Double> coords = new ArrayList<>();
        path.getNodes().forEach(n -> {
            var p = Geometry.toScreenCoords(Geometry.normalize(n.lat, n.lon));
            coords.add(p.x);
            coords.add(p.y);
        });
        line.getPoints().addAll(coords);

        Tooltip.setTooltip(line, path.toString(), path.getDescription(), "id: " + path.id);

        ApplicationConfig.showLinkPaths.addListener((e, o, n) -> {
            line.setVisible(n);
        });
        line.setVisible(ApplicationConfig.showLinkPaths.get());

        return line;
    }

    public static Pane pathNode(MapNode node) {
        if (Double.isNaN(node.lat) || Double.isNaN(node.lon)) {
            return null;
        }
        Pane pane = new Pane();
        pane.setPrefHeight(0);
        pane.setPrefWidth(0);

        var r = ApplicationConfig.mapPathNodeRadius;
        Ellipse ellipse = new Ellipse(r, r);
        ellipse.setId("street-map__path-node");

        pane.getChildren().add(ellipse);
        Geometry.relocateToScreenCoords(pane, node.getPos());

        ApplicationConfig.showPathNodes.addListener((e, o, n) -> {
            pane.setVisible(n);
        });
        pane.setVisible(ApplicationConfig.showPathNodes.get());

        return pane;
    }

    public static Pane addressNode(MapAddress address) {
        Pane pane = new Pane();
        pane.setPrefHeight(0);
        pane.setPrefWidth(0);
        double r = ApplicationConfig.addressSignRadius;
        Ellipse ellipse = new Ellipse(r, r);
        Text txt = new Text(address.housenumber);
        txt.relocate(-r / 2, -r / 2);

        pane.getChildren().addAll(ellipse, txt);

        pane.setId("street-map__circle-sign");
        ellipse.setId("street-map__circle-sign-outer");
        txt.setId("street-map__circle-sign-text");

        ellipse.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> {
            ellipse.setStroke(Color.RED);
            ellipse.fillProperty().set(Color.DARKSALMON);
        });
        ellipse.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> {
            ellipse.setStroke(Color.BLACK);
            ellipse.fillProperty().set(Color.ALICEBLUE);
        });
        txt.hoverProperty().subscribe((a, b) -> {
            if (a) {
                ellipse.setStroke(Color.BLACK);
                ellipse.fillProperty().set(Color.ALICEBLUE);
            }
            if (b) {
                ellipse.setStroke(Color.RED);
                ellipse.fillProperty().set(Color.DARKSALMON);
            }
        });

        for (var child : pane.getChildren()) {
            Tooltip.setTooltip(child, address.toString(), "", "id: " + address.id);
            child.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                if (!e.isSecondaryButtonDown()) {
                    return;
                }
                Options.clear();
                Options.addOption(evt -> StreetMapApp.setSrc(address), "set as source");
                Options.addOption(evt -> StreetMapApp.setDest(address), "set as dest");
                Geometry.relocateToScreenCoords(Options.getInstance(), address.node.getPos());
                Options.show();
                Tooltip.hide();
                e.consume();
            });
        }

        Geometry.relocateToScreenCoords(pane, address.node.getPos());
        return pane;
    }

}
