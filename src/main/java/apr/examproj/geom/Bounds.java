package apr.examproj.geom;

import apr.datastructures.graph.Point2D;

/**
 * Bounds
 */
public class Bounds {

    public double minLatitude, maxLatitude;
    public double minLongitude, maxLongitude;
    public double width, height;

    public Bounds(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) {
        this.minLatitude = minLatitude;
        this.maxLatitude = maxLatitude;
        this.minLongitude = minLongitude;
        this.maxLongitude = maxLongitude;
        this.width = maxLatitude - minLatitude;
        this.height = maxLongitude - minLongitude;
    }

    public Point2D normalize(double latitude, double longitude) {
        return new Point2D((latitude - minLatitude) / width, (longitude - minLongitude) / height);
    }

}
