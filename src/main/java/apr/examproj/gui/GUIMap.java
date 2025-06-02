package apr.examproj.gui;

import java.util.ArrayList;
import java.util.List;

import apr.datastructures.graph.Point2D;
import apr.examproj.application.StreetMapApp;
import apr.examproj.config.ApplicationConfig;
import apr.examproj.map.MapAddress;
import apr.examproj.map.MapBuilding;
import apr.examproj.map.MapNode;
import apr.examproj.map.MapPath;
import apr.examproj.map.StreetMap;
import apr.examproj.utils.Geometry;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;

/**
 * Map
 */
public class GUIMap extends Pane {

    static double pixelsPerDegree = 246153;
    static double width, height, ratio;

    public StreetMap streetMap;

    public GUIMap(StreetMap streetMap) {
        this.streetMap = streetMap;
        draw();
    }

    void draw() {
        width = streetMap.mapbounds.width * pixelsPerDegree;
        height = streetMap.mapbounds.height * pixelsPerDegree;
        ratio = width / height;

        setWidth(width);
        setHeight(height);

        Geometry.mapBounds = streetMap.mapbounds;
        Geometry.pixelWidth = width;
        Geometry.pixelHeight = height;

        getChildren().clear();

        for (var p : streetMap.paths) {
            getChildren().add(pathLine(p));
            for (var n : p.getNodes()) {
                var pn = pathNode(n);
                if (pn != null)
                    getChildren().add(pn);
            }
        }

        for (var b : streetMap.buildings) {
            getChildren().add(buildingPolygon(b));
        }

        for (var lp : streetMap.linkPaths) {
            getChildren().add(linkPath(lp));
        }

        streetMap.addresses.forEach(a -> getChildren().add(addressNode(a)));
    }

    public void zoom(double percentToZoom) {
        scaleXProperty().set(scaleXProperty().doubleValue() + percentToZoom);
        scaleYProperty().set(scaleYProperty().doubleValue() + percentToZoom);
    }

    Polyline pathLine(MapPath path) {
        Polyline line = new Polyline();
        line.setId("street-map__path");

        List<Point2D> pointsSeen = new ArrayList<>();
        List<Double> coords = new ArrayList<>();
        // System.out.println("GUIMap.pathLine(): ");
        path.getNodes().forEach(n -> {
            var p = toScreenCoords(normalize(n.lat, n.lon));

            if (Double.isNaN(p.x) || Double.isNaN(p.y)) {
                return;
            }

            for (var pp : pointsSeen) {
                if ((Math.abs(p.x - pp.x)) <= 5 && (Math.abs(p.y - pp.y)) <= 5) {
                    return;
                }
            }
            pointsSeen.add(p);

            // System.out.printf("(%.1f, %.1f),", p.x, p.y);

            coords.add(p.x);
            coords.add(p.y);
        });
        line.getPoints().addAll(coords);
        // System.out.println();

        Tooltip.setTooltip(line, path.toString(), path.getDescription(), "id: " + path.id);

        return line;
    }

    Polygon buildingPolygon(MapBuilding building) {
        Polygon polygon = new Polygon();
        polygon.setId("street-map__building");

        List<Double> coords = new ArrayList<>();
        building.getNodes().forEach(n -> {
            var p = toScreenCoords(normalize(n.lat, n.lon));
            coords.add(p.x);
            coords.add(p.y);
        });
        polygon.getPoints().addAll(coords);

        return polygon;
    }

    Polyline linkPath(MapPath path) {
        Polyline line = new Polyline();
        line.setId("street-map__link-path");

        List<Double> coords = new ArrayList<>();
        path.getNodes().forEach(n -> {
            var p = toScreenCoords(normalize(n.lat, n.lon));
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

    Pane pathNode(MapNode node) {
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
        relocateToScreenCoords(pane, node.getPos());

        ApplicationConfig.showPathNodes.addListener((e, o, n) -> {
            pane.setVisible(n);
        });
        pane.setVisible(ApplicationConfig.showPathNodes.get());

        return pane;
    }

    Pane addressNode(MapAddress address) {
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
                relocateToScreenCoords(Options.getInstance(), address.node.getPos());
                Options.show();
                Tooltip.hide();
                e.consume();
            });
        }

        relocateToScreenCoords(pane, address.node.getPos());
        return pane;
    }

    public void relocateToScreenCoords(Node javafxNode, Point2D worldCoords) {
        var p = toScreenCoords(normalize(worldCoords.x, worldCoords.y));
        javafxNode.relocate(p.x, p.y);
    }

    public Point2D toScreenCoords(Point2D coords) {
        return new Point2D(coords.y * height, width - coords.x * width);
    }

    public Point2D normalize(double latitude, double longitude) {
        return new Point2D((latitude - streetMap.mapbounds.minLatitude) / streetMap.mapbounds.width,
                (longitude - streetMap.mapbounds.minLongitude) / streetMap.mapbounds.height);
    }

}
