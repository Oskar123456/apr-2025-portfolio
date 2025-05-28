package apr.examproj.utils;

import apr.examproj.map.MapNode;

/**
 * Geometry
 */
public class Geometry {

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

}
