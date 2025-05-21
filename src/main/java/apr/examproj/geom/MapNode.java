package apr.examproj.geom;

import org.jsoup.nodes.Element;

/**
 * Node
 */
public class MapNode {

    public String id;
    public double lat, lon;

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

}
