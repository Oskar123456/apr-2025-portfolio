package apr.examproj.geom;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import apr.datastructures.graph.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

/**
 * GeomFactory
 */
public class GeomFactory {

    public static Polyline polyline(MapBounds mapBounds, double width, double height, List<MapNode> nodes, String id) {
        List<Double> acc = new ArrayList<>();
        nodes.stream()
                // .filter(n -> mapBounds.isInBounds(n))
                .map(n -> mapBounds.normalize(new Point2D(n.lat, n.lon)))
                .forEach((p) -> {
                    acc.add(p.x * width);
                    acc.add(p.y * height);
                });
        Polyline poly = new Polyline(ArrayUtils.toPrimitive(acc.toArray(Double[]::new)));
        poly.setId(id);
        return poly;
    }

    public static Polygon polygon(MapBounds mapBounds, double width, double height, List<MapNode> nodes, String id) {
        List<Double> acc = new ArrayList<>();
        nodes.stream()
                .filter(n -> mapBounds.isInBounds(n))
                .map(n -> mapBounds.normalize(new Point2D(n.lat, n.lon)))
                .forEach((p) -> {
                    acc.add(p.x * width);
                    acc.add(p.y * height);
                });
        Polygon poly = new Polygon(ArrayUtils.toPrimitive(acc.toArray(Double[]::new)));
        poly.setId(id);
        return poly;
    }

    public static Ellipse dot(MapBounds mapBounds, double width, double height, double r, MapNode node, String id) {
        if (!mapBounds.isInBounds(node)) {
            return new Ellipse();
        }
        double rScale = Math.min(width, height);
        Ellipse dot = new Ellipse(r * rScale, r * rScale);
        dot.setId(id);
        var pos = mapBounds.normalize(node.lat, node.lon);
        dot.relocate(pos.x * width, pos.y * height);
        return dot;
    }

    public static Pane container(MapBounds mapBounds, double width, double height, MapNode node, String id) {
        if (!mapBounds.isInBounds(node)) {
            return new Pane();
        }
        double rScale = Math.min(width, height);
        Pane pane = new Pane();
        pane.setId(id);
        var pos = mapBounds.normalize(node.lat, node.lon);
        pane.relocate(pos.x * width, pos.y * height);
        return pane;
    }

}
