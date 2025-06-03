package apr.examproj.gui;

import apr.datastructures.graph.Point2D;
import apr.examproj.map.StreetMap;
import apr.examproj.utils.Geometry;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
        width = streetMap.bounds.width * pixelsPerDegree;
        height = streetMap.bounds.height * pixelsPerDegree;
        ratio = width / height;

        setWidth(width);
        setHeight(height);

        Geometry.mapBounds = streetMap.bounds;
        Geometry.pixelWidth = width;
        Geometry.pixelHeight = height;

        getChildren().clear();

        for (var p : streetMap.paths) {
            getChildren().add(GUIFactory.pathLine(p));
            for (var n : p.getNodes()) {
                var pn = GUIFactory.pathNode(n);
                if (pn != null)
                    getChildren().add(pn);
            }
        }

        for (var b : streetMap.buildings) {
            getChildren().add(GUIFactory.buildingPolygon(b));
        }

        for (var lp : streetMap.linkPaths) {
            getChildren().add(GUIFactory.linkPath(lp));
        }

        streetMap.addresses.forEach(a -> getChildren().add(GUIFactory.addressNode(a)));

        drawRuler();
    }

    public void zoom(double percentToZoom) {
        scaleXProperty().set(scaleXProperty().doubleValue() + percentToZoom);
        scaleYProperty().set(scaleYProperty().doubleValue() + percentToZoom);
    }

    void drawRuler() {
        var widthFraction = 0.2;

        HBox ruler = new HBox();
        ruler.setId("street-map__ruler");
        ruler.prefWidthProperty().bind(widthProperty().multiply(widthFraction));
        ruler.translateXProperty().set(5);
        ruler.translateYProperty().set(5);

        Text text = new Text(rulerText(widthFraction));
        widthProperty().addListener((e) -> {
            text.setText(rulerText(widthFraction));
        });
        text.setId("street-map__ruler-text");

        ruler.getChildren().add(text);
        getChildren().add(ruler);
    }

    String rulerText(double widthFraction) {
        var leftCorner = new Point2D(streetMap.bounds.minLatitude, streetMap.bounds.minLatitude);
        var rightCorner = new Point2D(streetMap.bounds.minLatitude
                + (streetMap.bounds.width) * (getWidth() / (streetMap.bounds.width * pixelsPerDegree)),
                streetMap.bounds.minLongitude);
        var realWorldDist = Geometry.greatCicleDistance(leftCorner,
                Point2D.lerp(leftCorner, rightCorner, widthFraction));
        return String.format("<- %.1fm ->", realWorldDist);
    }

}
