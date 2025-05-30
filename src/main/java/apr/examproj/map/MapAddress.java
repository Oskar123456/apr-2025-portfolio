package apr.examproj.map;

import apr.examproj.application.StreetMapApp;
import apr.examproj.config.ApplicationConfig;
import apr.examproj.gui.GUIFactory;
import apr.examproj.gui.GUIMapConfig;
import apr.examproj.gui.GUIUtils;
import apr.examproj.gui.IGUIMapElement;
import apr.examproj.gui.Options;
import apr.examproj.gui.Tooltip;
import apr.examproj.utils.Geometry;
import javafx.scene.input.MouseEvent;
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

        for (var child : sign.getChildren()) {
            Tooltip.setTooltip(child, toString(), "", "id: " + id);
            child.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                if (!e.isSecondaryButtonDown()) {
                    return;
                }
                Options.clear();
                Options.addOption(evt -> StreetMapApp.setSrc(this), "set as source");
                Options.addOption(evt -> StreetMapApp.setDest(this), "set as dest");
                Geometry.relocateToScreenCoords(Options.getInstance(), node.getPos());
                // var pos = GUIUtils.mapNodeCoordsToPane(bounds, renderPane, node);
                // Options.getInstance().relocate(pos.x, pos.y);
                Options.show();
                Tooltip.hide();
                e.consume();
            });
        }

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
