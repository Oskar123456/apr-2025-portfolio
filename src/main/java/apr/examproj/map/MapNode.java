package apr.examproj.map;

import apr.examproj.gui.IGUIMapElement;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;

/**
 * MapNode
 */
public class MapNode implements IGUIMapElement {

    static String cssClassName = "street-map__node";

    public String id;
    public double lat, lon;
    public double radius = 0.005;

    public MapNode() {
    }

    public MapNode(String id, double latitude, double longitude) {
        this.id = id;
        this.lat = latitude;
        this.lon = longitude;
    }

    public String toString() {
        return String.format("Node[id: %s, lat: %f, lon: %f]", id, lat, lon);
    }

    @Override
    public void draw(MapBounds bounds, Pane parentPane) {
        if (!bounds.isInBounds(this)) {
            return;
        }
        double rScale = Math.min(parentPane.getWidth(), parentPane.getHeight());
        Ellipse dot = new Ellipse(radius * rScale, radius * rScale);
        dot.setId(cssClassName);
        var pos = bounds.normalize(lat, lon);
        parentPane.getChildren().add(dot);
        dot.relocate(pos.x * parentPane.getWidth(), pos.y * parentPane.getHeight());
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
