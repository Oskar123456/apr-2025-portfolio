package apr.examproj.utils;

import apr.datastructures.graph.Point2D;
import apr.examproj.config.ApplicationConfig;
import apr.examproj.enums.TransportationMode;
import apr.examproj.map.MapBounds;
import apr.examproj.map.MapNode;
import javafx.scene.Node;

/**
 * Geometry
 */
public class Geometry {

    public static MapBounds mapBounds;

    public static double pixelWidth, pixelHeight;

    /**
     * In meters.
     */
    public static double greatCicleDistance(MapNode src, MapNode dest) {
        double R = 6371e3;
        double minLatRad = src.lat * Math.PI / 180;
        double maxLatRad = dest.lat * Math.PI / 180;
        double latArcRad = Math.abs(dest.lat - src.lat) * Math.PI / 180;
        double lonArcRad = Math.abs(dest.lon - src.lon) * Math.PI / 180;

        double a = Math.sin(latArcRad / 2) * Math.sin(latArcRad / 2) +
                Math.cos(minLatRad) * Math.cos(maxLatRad) *
                        Math.sin(lonArcRad / 2) * Math.sin(lonArcRad / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public static Point2D closestPoint(Point2D A, Point2D B, Point2D C) {
        var AB = B.subtract(A);
        var BC = C.subtract(B);
        var CA = A.subtract(C);

        System.out.printf("Geometry.closestPoint(): A: %s, B: %s, C: %s%n",
                A.toString(), B.toString(), C.toString());

        System.out.printf("Geometry.closestPoint(): AB: %s, BC: %s, CA: %s%n",
                AB.toString(), BC.toString(), CA.toString());

        Point2D BCOrth = new Point2D(-BC.y, BC.x).normalize();

        System.out.printf("Geometry.closestPoint(): orth(AB) = %s%n",
                BCOrth);

        double ABLen = AB.magnitude();
        double BCLen = BC.magnitude();
        double CALen = CA.magnitude();

        double DLen = CALen * ((ABLen * ABLen + BCLen * BCLen - CALen * CALen) / (2 * ABLen * BCLen));

        System.out.printf("Geometry.closestPoint(): |AB| = %f, |BC| = %f, |CA| = %f, |D| = %f%n",
                ABLen, BCLen, CALen, DLen);

        Point2D D = BCOrth.scale(DLen);

        System.out.println("Geometry.closestPoint(): D = " + D.toString());

        Point2D p = A.add(D);

        System.out.println("Geometry.closestPoint(): D + p = " + p);

        double xMin = Math.min(B.x, C.x);
        double xMax = Math.max(B.x, C.x);
        double yMin = Math.min(B.y, C.y);
        double yMax = Math.max(B.y, C.y);
        if (p.x < xMin || p.x > xMax || p.y < yMin || p.y > yMax) {
            System.out.print("Geometry.closestPoint(): out of bounds: " + p.toString());
            p.x = B.x;
            p.y = B.y;
            System.out.println(", change to: " + p.toString());
        }

        return p;
    }

    public static void relocateToScreenCoords(Node javafxNode, Point2D worldCoords) {
        var p = toScreenCoords(normalize(worldCoords.x, worldCoords.y));
        javafxNode.relocate(p.x, p.y);
    }

    public static Point2D toScreenCoords(Point2D coords) {
        return new Point2D(coords.y * pixelHeight, pixelWidth - coords.x * pixelWidth);
    }

    public static Point2D normalize(double latitude, double longitude) {
        return new Point2D((latitude - mapBounds.minLatitude) / mapBounds.width,
                (longitude - mapBounds.minLongitude) / mapBounds.height);
    }

    public static double travelTime(MapNode src, MapNode dest, TransportationMode transportationMode) {
        switch (transportationMode) {
            default:
                return (src.dist(dest) / 1000D) / ApplicationConfig.getWalkingSpeed();
        }
    }

}
