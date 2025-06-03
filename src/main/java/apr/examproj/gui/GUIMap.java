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

        widthProperty().addListener(e -> updateRulerText());
        heightProperty().addListener(e -> updateRulerText());

        scaleXProperty().addListener(e -> updateRulerText());
    }

    public void zoom(double percentToZoom) {
        scaleXProperty().set(scaleXProperty().doubleValue() + percentToZoom);
        scaleYProperty().set(scaleYProperty().doubleValue() + percentToZoom);
    }

    void updateRulerText() {
        double rulerPixelWidth = Ruler.getInstance().getWidth();
        double mapPixelWidth = getWidth();
        double mapMeterWidth = Geometry.greatCicleDistance(
                new Point2D(streetMap.bounds.minLatitude, streetMap.bounds.minLongitude),
                new Point2D(streetMap.bounds.maxLatitude, streetMap.bounds.minLongitude));
        double metersPerPixel = mapMeterWidth / (mapPixelWidth * scaleXProperty().doubleValue());
        double rulerMeterWidth = metersPerPixel * rulerPixelWidth;
        // System.out.printf(
        // "GUIMap.updateRulerText(): rulerPixelWidth %.1f, "
        // + " mapPixelWidth %.1f,"
        // + " mapscale %.1f,"
        // + " mapMeterWidth %.1f,"
        // + " metersPerPixel %.1f,"
        // + " rulerMeterWidth %.1f %n",
        // rulerPixelWidth,
        // mapPixelWidth,
        // scaleXProperty().doubleValue(),
        // mapMeterWidth,
        // metersPerPixel,
        // rulerMeterWidth);
        // Ruler.setText(String.format("%.1fm", rulerMeterWidth));
        Ruler.setText(String.format("scale %.2f", scaleXProperty().doubleValue()));
    }

}
