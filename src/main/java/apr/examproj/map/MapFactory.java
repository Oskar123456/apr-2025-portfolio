package apr.examproj.map;

import java.util.List;

import org.jsoup.nodes.Element;

import apr.examproj.osm.MapData;
import apr.examproj.utils.Stringify;

/**
 * MapFactory
 */
public class MapFactory {

    public static MapBounds bounds(Element xmlElmt) {
        MapBounds bounds = new MapBounds();
        bounds.minLatitude = Double.valueOf(xmlElmt.attributes().get("minlat"));
        bounds.maxLatitude = Double.valueOf(xmlElmt.attributes().get("maxlat"));
        bounds.minLongitude = Double.valueOf(xmlElmt.attributes().get("minlon"));
        bounds.maxLongitude = Double.valueOf(xmlElmt.attributes().get("maxlon"));
        bounds.width = bounds.maxLatitude - bounds.minLatitude;
        bounds.height = bounds.maxLongitude - bounds.minLongitude;
        return bounds;
    }

    public static MapNode node(Element xmlElmt) {
        MapNode node = new MapNode();
        node.id = xmlElmt.id();
        node.lat = Double.valueOf(xmlElmt.attributes().get("lat"));
        node.lon = Double.valueOf(xmlElmt.attributes().get("lon"));
        return node;
    }

    public static MapAddress address(Element xmlElmt) {
        MapAddress address = new MapAddress();
        var tags = MapData.extractTags(xmlElmt);
        address.id = tags.get("osak:identifier");
        address.city = tags.get("addr:city");
        address.country = tags.get("addr:country");
        address.housenumber = tags.get("addr:housenumber");
        address.municipality = tags.get("addr:municipality");
        address.postcode = tags.get("addr:postcode");
        address.street = tags.get("addr:street");
        address.node = node(xmlElmt);
        return address;
    }

    public static MapBuilding building(Element xmlElmt) {
        MapBuilding building = new MapBuilding();

        return building;
    }

    public static MapPath path(Element xmlElmt, List<MapNode> nodes) {
        MapPath path = new MapPath();

        var tags = MapData.extractTags(xmlElmt);
        path.id = xmlElmt.id();
        path.name = tags.containsKey("name") ? tags.get("name") : "";
        path.type = tags.containsKey("highway") ? tags.get("highway") : "";
        try {
            path.maxSpeed = Double.valueOf(tags.get("maxspeed"));
        } catch (Exception e) {
            path.maxSpeed = 5;
        }

        for (var nd : xmlElmt.getElementsByTag("nd")) {
            for (var node : nodes) {
                if (node.id.equals(nd.attributes().get("ref"))) {
                    path.addNode(node);
                }
            }
        }

        System.out.println("MapFactory.path() : " + Stringify.toString(path));

        return path;
    }

    public static MapWay way(Element xmlElmt) {
        MapWay way = new MapWay();

        return way;
    }

}
