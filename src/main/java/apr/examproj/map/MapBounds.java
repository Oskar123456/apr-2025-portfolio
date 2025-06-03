package apr.examproj.map;

import org.jsoup.nodes.Element;

import apr.datastructures.graph.Point2D;
import apr.examproj.gui.IGUIMapElement;
import apr.examproj.utils.Geometry;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Bounds
 */
public class MapBounds implements IGUIMapElement {

    public double origMinLatitude, origMinLongitude;
    public double origMaxLatitude, origMaxLongitude;
    public double minLatitude, maxLatitude;
    public double minLongitude, maxLongitude;
    public double width, height;
    public double ratio;

    static double zoomSpeed = 0.000005;
    static double moveSpeed = 0.000005;

    public MapBounds() {
    }

    public MapBounds(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
        this.origMinLatitude = minLatitude;
        this.origMaxLatitude = maxLatitude;
        this.origMinLongitude = minLongitude;
        this.origMaxLongitude = maxLongitude;
        this.width = maxLatitude - minLatitude;
        this.height = maxLongitude - minLongitude;
        this.ratio = width / height;
    }

    public MapBounds(Element xmlElmt) {
        this.minLatitude = Double.valueOf(xmlElmt.attributes().get("minlat"));
        this.maxLatitude = Double.valueOf(xmlElmt.attributes().get("maxlat"));
        this.minLongitude = Double.valueOf(xmlElmt.attributes().get("minlon"));
        this.maxLongitude = Double.valueOf(xmlElmt.attributes().get("maxlon"));
        this.origMinLatitude = minLatitude;
        this.origMaxLatitude = maxLatitude;
        this.origMinLongitude = minLongitude;
        this.origMaxLongitude = maxLongitude;
        this.width = maxLatitude - minLatitude;
        this.height = maxLongitude - minLongitude;
        this.ratio = height / width;
    }

    public void move(double x, double y) {
        double xMove = -x * moveSpeed;
        double yMove = -y * moveSpeed;
        if (minLatitude + xMove < origMinLatitude || maxLatitude + xMove > origMaxLatitude
                || minLongitude + yMove < origMinLongitude || maxLongitude + yMove > origMaxLongitude) {
            return;
        }
        minLatitude += xMove;
        maxLatitude += xMove;
        minLongitude += yMove;
        maxLongitude += yMove;
    }

    public void zoom(double value) {
        minLatitude += value * zoomSpeed;
        maxLatitude -= value * zoomSpeed;
        minLongitude += (value * zoomSpeed) * ratio;
        maxLongitude -= (value * zoomSpeed) * ratio;
        minLatitude = Math.max(origMinLatitude, minLatitude);
        maxLatitude = Math.min(origMaxLatitude, maxLatitude);
        minLongitude = Math.max(origMinLongitude, minLongitude);
        maxLongitude = Math.min(origMaxLongitude, maxLongitude);
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
        var widthFraction = 0.1;
        var leftCorner = new Point2D(bounds.minLatitude, bounds.minLongitude);
        var rightCorner = new Point2D(bounds.maxLatitude, bounds.minLongitude);
        var realWorldDist = Geometry.greatCicleDistance(leftCorner,
                Point2D.lerp(leftCorner, rightCorner, widthFraction));

        HBox ruler = new HBox();
        ruler.setId("street-map__ruler");
        ruler.prefWidthProperty().bind(renderPane.widthProperty().multiply(widthFraction));
        ruler.prefHeightProperty().set(40);
        ruler.translateXProperty().set(20);
        ruler.translateYProperty().set(20);

        Text text = new Text(String.format("<- %.1fm ->", realWorldDist));
        text.setId("street-map__ruler-text");

        ruler.getChildren().add(text);
        renderPane.getChildren().add(ruler);
    }

}
