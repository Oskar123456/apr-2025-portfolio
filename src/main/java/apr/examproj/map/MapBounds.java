package apr.examproj.map;

import org.jsoup.nodes.Element;

import apr.datastructures.graph.Point2D;
import apr.examproj.gui.IGUIMapElement;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * Bounds
 */
public class MapBounds implements IGUIMapElement {

    public double minLatitude, maxLatitude;
    public double minLongitude, maxLongitude;
    public double width, height;

    public MapBounds() {
    }

    public MapBounds(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
        this.width = maxLatitude - minLatitude;
        this.height = maxLongitude - minLongitude;
    }

    public MapBounds(Element xmlElmt) {
        this.minLatitude = Double.valueOf(xmlElmt.attributes().get("minlat"));
        this.maxLatitude = Double.valueOf(xmlElmt.attributes().get("maxlat"));
        this.minLongitude = Double.valueOf(xmlElmt.attributes().get("minlon"));
        this.maxLongitude = Double.valueOf(xmlElmt.attributes().get("maxlon"));
        this.width = maxLatitude - minLatitude;
        this.height = maxLongitude - minLongitude;
    }

    public Point2D normalize(double latitude, double longitude) {
        return new Point2D((latitude - minLatitude) / width, (longitude - minLongitude) / height);
    }

    public Point2D normalize(Point2D point) {
        return new Point2D((point.x - minLatitude) / width, (point.y - minLongitude) / height);
    }

    public boolean isInBounds(Point2D point) {
        return point.x >= minLatitude && point.x <= maxLatitude && point.y >= minLongitude && point.y <= maxLongitude;
    }

    public boolean isInBounds(double latitude, double longitude) {
        return latitude >= minLatitude && latitude <= maxLatitude && longitude >= minLongitude
                && longitude <= maxLongitude;
    }

    public boolean isInBounds(MapNode node) {
        return isInBounds(node.lat, node.lon);
    }

    public double diagonalLength() {
        double R = 6371e3;
        double minLatRad = minLatitude * Math.PI / 180;
        double maxLatRad = maxLatitude * Math.PI / 180;
        double latArcRad = (maxLatitude - minLatitude) * Math.PI / 180;
        double lonArcRad = (maxLongitude - minLongitude) * Math.PI / 180;

        double a = Math.sin(latArcRad / 2) * Math.sin(latArcRad / 2) +
                Math.cos(minLatRad) * Math.cos(maxLatRad) *
                        Math.sin(lonArcRad / 2) * Math.sin(lonArcRad / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    @Override
    public String toString() {
        return String.format("%s[minLat: %f, minLon: %f, maxLat: %f, maxLon: %f]",
                getClass().getSimpleName(), minLatitude, minLongitude, maxLatitude, maxLongitude);
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

}
