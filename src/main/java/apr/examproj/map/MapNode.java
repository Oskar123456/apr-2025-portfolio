package apr.examproj.map;

import java.util.List;

import apr.datastructures.graph.Point2D;
import apr.datastructures.graph.TwoTuple;
import apr.examproj.config.ApplicationConfig;
import apr.examproj.gui.GUIFactory;
import apr.examproj.gui.IGUIMapElement;
import javafx.scene.layout.Pane;

/**
 * MapNode
 */
public class MapNode implements IGUIMapElement {

    public String id;
    public double lat, lon;

    public MapNode() {
    }

    public MapNode(String id) {
    }

    public MapNode(String id, double latitude, double longitude) {
        this.id = id;
        this.lat = latitude;
        this.lon = longitude;
    }

    public MapNode findClosest(List<MapNode> nodes) {
        MapNode closest = nodes.get(0);
        for (var node : nodes) {
            if (dist(node) < dist(closest)) {
                closest = node;
            }
        }
        return closest;
    }

    public Point2D middlePoint(MapNode other) {
        double newLat = (Math.max(lat, other.lat) - Math.min(lat, other.lat)) / 2 + Math.min(lat, other.lat);
        double newLon = (Math.max(lon, other.lon) - Math.min(lon, other.lon)) / 2 + Math.min(lon, other.lon);
        return new Point2D(newLat, newLon);
    }

    public TwoTuple<MapNode, MapNode> findClosestPair(List<MapNode> nodes) {
        MapNode closest1 = nodes.get(0);
        MapNode closest2 = nodes.get(1);
        for (var node : nodes) {
            if (dist(node) < dist(closest1)) {
                closest2 = closest1;
                closest1 = node;
            } else if (dist(node) < dist(closest2)) {
                closest2 = node;
            }
        }
        return new TwoTuple<>(closest1, closest2);
    }

    public double dist(MapNode other) {
        return Math.sqrt((this.lat - other.lat) * (this.lat - other.lat)
                + (this.lon - other.lon) * (this.lon - other.lon));
    }

    public Point2D getPos() {
        return new Point2D(lat, lon);
    }

    public String toString() {
        return String.format("Node[id: %s]", id.substring(0, 5));
        // return String.format("Node[id: %s, lat: %f, lon: %f]", id, lat, lon);
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        if (!bounds.isInBounds(this)) {
            return;
        }

        // var pos = bounds.normalize(lat, lon);
        //
        // Pane pane = new Pane();
        // pane.relocate(pos.x * renderPane.getWidth(), pos.y * renderPane.getHeight());
        //
        // Ellipse dot = new Ellipse(getRadius(renderPane), getRadius(renderPane));
        // dot.setId(ApplicationConfig.cssIdMapNode);
        // pane.getChildren().add(dot);
        // renderPane.getChildren().add();
        GUIFactory.defaultMapNode(renderPane, bounds, this);
    }

    public static double getRadius(Pane renderPane) {
        double rScale = Math.min(renderPane.getWidth(), renderPane.getHeight());
        return ApplicationConfig.mapNodeRadius * rScale;
    }

}
