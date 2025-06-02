package apr.examproj.utils;

import java.util.List;
import java.util.UUID;

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

    public static MapNode addNewClosestNode(MapNode node, List<MapNode> nodes) {
        int closestIdx = 0;
        int secondClosestIdx = 0;

        for (int i = 0; i < nodes.size(); i++) {
            var n = nodes.get(i);
            if (n.dist(node) < nodes.get(closestIdx).dist(node)) {
                closestIdx = i;
            }
        }

        if (closestIdx == 0) {
            secondClosestIdx = closestIdx + 1;
        } else if (closestIdx + 1 == nodes.size()) {
            secondClosestIdx = closestIdx - 1;
        } else {
            secondClosestIdx = (nodes.get(closestIdx - 1).dist(node) < nodes.get(closestIdx + 1).dist(node))
                    ? closestIdx - 1
                    : closestIdx + 1;
        }

        var newPoint = closestPointBinSearch(node.getPos(),
                nodes.get(closestIdx).getPos(),
                nodes.get(secondClosestIdx).getPos());
        var newNode = new MapNode(UUID.randomUUID().toString(), newPoint.x, newPoint.y);
        nodes.add(secondClosestIdx, newNode);
        return newNode;
    }

    public static Point2D projection(Point2D C, Point2D B, Point2D A) {
        var AB = B.subtract(A);
        var AC = C.subtract(A);
        var AD = AB.mul(AB.dot(AC)).mul(1 / AB.dot(AB));
        var D = A.add(AD);

        var xMin = Math.min(A.x, B.x);
        var xMax = Math.max(A.x, B.x);
        var yMin = Math.min(A.y, B.y);
        var yMax = Math.max(A.y, B.y);

        if (D.x < xMin || D.x > xMax || D.y < yMin || D.y > yMax) {
            if (C.dist(A) < C.dist(B)) {
                return new Point2D(A.x, A.y);
            } else {
                return new Point2D(B.x, B.y);
            }
        }

        return D;
    }

    public static Point2D closestPointBinSearch(Point2D A, Point2D B, Point2D C) {
        int tries = 2;

        var P = new Point2D(B.x, B.y);
        var P1 = B;
        var P2 = C;

        System.out.printf("Geometry.closestPointBinSearch(): %s %s %s%n",
                A.toString(), B.toString(), C.toString());

        for (int i = 0; i < tries; i++) {
            var xMin = Math.min(P1.x, P2.x);
            var xMax = Math.max(P1.x, P2.x);
            var yMin = Math.min(P1.y, P2.y);
            var yMax = Math.max(P1.y, P2.y);
            P = new Point2D((xMax - xMin) / 2 + xMin, (yMax - yMin) / 2 + yMin);
            System.out.printf("Geometry.closestPointBinSearch(): midpoint between %s %s = %s%n",
                    P1.toString(), P2.toString(), P.toString());
            if (A.dist(P1) < A.dist(P2)) {
                P2 = P;
            } else {
                P1 = P;
            }
        }

        System.out.printf("Geometry.closestPointBinSearch(): P = %s%n", P.toString());

        return P;
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
