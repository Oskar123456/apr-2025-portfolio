package apr.examproj.geom;

import org.jsoup.nodes.Element;

import apr.examproj.gui.GUIMapElement;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;

/**
 * Node
 */
public class MapNode implements GUIMapElement {

    public String id;
    public double lat, lon;

    double r = 0.005;

    public MapNode(String id, double latitude, double longitude) {
        this.id = id;
        this.lat = latitude;
        this.lon = longitude;
    }

    public MapNode(Element xmlElmt) {
        this.id = xmlElmt.id();
        this.lat = Double.valueOf(xmlElmt.attributes().get("lat"));
        this.lon = Double.valueOf(xmlElmt.attributes().get("lon"));
    }

    public String toString() {
        return String.format("Node[id: %s, lat: %f, lon: %f]", id, lat, lon);
    }

    @Override
    public Node guify(Pane parentPane, MapBounds mapBounds) {
        if (!mapBounds.isInBounds(this)) {
            return new Ellipse();
        }
        double rScale = Math.min(parentPane.getWidth(), parentPane.getHeight());
        Ellipse dot = new Ellipse(r * rScale, r * rScale);
        dot.setId("street-map__street-map-node");
        var pos = mapBounds.normalize(lat, lon);
        parentPane.getChildren().add(dot);
        dot.relocate(pos.x * parentPane.getWidth(), pos.y * parentPane.getHeight());
        return dot;
    }

}
