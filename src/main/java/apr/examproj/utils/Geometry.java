package apr.examproj.utils;

import apr.datastructures.graph.Point2D;
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

    public static Point2D closestPoint(Point2D fromP, Point2D toA, Point2D toB) {
        Point2D aVec = toA.subtract(toB);
        Point2D bVec = toB.subtract(fromP);
        Point2D cVec = fromP.subtract(toA);

        Point2D paraVec = toB.subtract(toA);
        Point2D perpVec = new Point2D(-paraVec.y, paraVec.x).normalize();

        double aLen = toA.subtract(toB).magnitude();
        double bLen = toB.subtract(fromP).magnitude();
        double cLen = fromP.subtract(toA).magnitude();

        double dLen = cLen * ((aLen * aLen + bLen * bLen - cLen * cLen) / (2 * aLen * bLen));

        Point2D dVec = perpVec.scale(dLen);
        Point2D p = fromP.add(dVec);

        double xMin = Math.min(toA.x, toB.x);
        double xMax = Math.max(toA.x, toB.x);
        double yMin = Math.min(toA.y, toB.y);
        double yMax = Math.max(toA.y, toB.y);
        if (p.x < xMin || p.x > xMax || p.y < yMin || p.y > yMax) {
            p.x = toA.x;
            p.y = toA.y;
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

}
