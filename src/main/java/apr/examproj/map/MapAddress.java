package apr.examproj.map;

import apr.examproj.gui.GUIFactory;
import apr.examproj.gui.GUIMapConfig;
import apr.examproj.gui.GUIUtils;
import apr.examproj.gui.IGUIMapElement;
import apr.examproj.gui.Tooltip;
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
        var sign = GUIFactory.defaultCircleSign(housenumber,
                GUIMapConfig.getInstance().getAddrRadius() * renderPane.getWidth());
        var point = GUIUtils.mapNodeCoordsToPane(bounds, renderPane, node);
        sign.relocate(point.x, point.y);

        sign.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> showTooltip(renderPane, e));
        sign.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> hideTooltip(renderPane));

        renderPane.getChildren().addAll(sign);
    }

    private void hideTooltip(Pane renderPane) {
        Tooltip.getInstance().setVisible(false);
    }

    private void showTooltip(Pane renderPane, MouseEvent event) {
        Tooltip.getInstance().setTitle(toString());
        Tooltip.getInstance().clearContent();
        Tooltip.getInstance().setFootnote(id);
        Tooltip.getInstance().setVisible(true);
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s", street != null ? street.toString() : "", housenumber, postcode, city,
                country);
    }

}
