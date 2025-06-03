package apr.examproj.gui;

import apr.examproj.map.StreetMap;
import apr.examproj.utils.Geometry;
import javafx.scene.layout.Pane;

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
    }

    public void zoom(double percentToZoom) {
        scaleXProperty().set(scaleXProperty().doubleValue() + percentToZoom);
        scaleYProperty().set(scaleYProperty().doubleValue() + percentToZoom);
    }

}
