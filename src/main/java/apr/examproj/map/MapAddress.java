package apr.examproj.map;

import apr.examproj.gui.GUIFactory;
import apr.examproj.gui.GUIMapConfig;
import apr.examproj.gui.GUIUtils;
import apr.examproj.gui.IGUIMapElement;
import javafx.scene.Node;
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
    public String street;
    public String housenumber;
    public MapNode node;

    public MapAddress() {
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        var sign = GUIFactory.defaultCircleSign(housenumber,
                GUIMapConfig.getInstance().getAddrRadius() * renderPane.getWidth());
        var point = GUIUtils.mapNodeCoordsToPane(bounds, renderPane, node);
        sign.relocate(point.x, point.y);
        renderPane.getChildren().add(sign);
    }

    @Override
    public Node tooltip(Pane parentPane) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tooltip'");
    }

    @Override
    public void setHoverTooltipTarget(Pane tooltipTarget) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setHoverTooltipTarget'");
    }

}
