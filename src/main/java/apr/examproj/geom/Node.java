package apr.examproj.geom;

/**
 * Node
 */
public class Node {

    public String id;
    public double latitude, longitude; // x , y

    public Node(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString() {
        return String.format("Node[id: %s, lat: %f, lon: %f]", id, latitude, longitude);
    }

}
