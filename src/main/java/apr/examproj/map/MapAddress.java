package apr.examproj.map;

import apr.examproj.config.ApplicationConfig;
import apr.examproj.gui.GUIFactory;
import apr.examproj.gui.GUIUtils;
import apr.examproj.gui.IGUIMapElement;
import javafx.scene.layout.Pane;

/**
 * MapAddress
 */
public class MapAddress implements IGUIMapElement {

    public String id;
    public String city;
    public String country;
    public String municipality;
    public String postcode;
    public String housenumber;
    public MapNode node;
    public MapPath street;

    public MapAddress() {
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        var sign = GUIFactory.defaultCircleSign(housenumber, getRadius(renderPane));
        var point = GUIUtils.mapNodeCoordsToPane(bounds, renderPane, node);
        sign.relocate(point.x, point.y);

        renderPane.getChildren().addAll(sign);
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s", street != null ? street.toString() : "", housenumber, postcode, city,
                country);
    }

    public static double getRadius(Pane renderPane) {
        double rScale = Math.min(renderPane.getWidth(), renderPane.getHeight());
        return Math.min(ApplicationConfig.mapAddressRadius * rScale, ApplicationConfig.mapAddressRadiusMax);
    }

}
