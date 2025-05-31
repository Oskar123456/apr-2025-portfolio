package apr.examproj.map;

import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;

import apr.examproj.osm.MapData;

/**
 * MapFactory
 */
public class MapFactory {

    public static MapBounds bounds(Element xmlElmt) {
        MapBounds bounds = new MapBounds(xmlElmt);
        return bounds;
    }

    public static MapNode node(Element xmlElmt) {
        MapNode node = new MapNode();
        node.id = xmlElmt.id();
        node.lat = Double.valueOf(xmlElmt.attributes().get("lat"));
        node.lon = Double.valueOf(xmlElmt.attributes().get("lon"));
        return node;
    }

    public static MapAddress address(Element xmlElmt, List<MapPath> paths) {
        MapAddress address = new MapAddress();
        var tags = MapData.extractTags(xmlElmt);
        address.id = tags.get("osak:identifier");
        address.city = tags.get("addr:city");
        address.country = tags.get("addr:country");
        address.housenumber = tags.get("addr:housenumber");
        address.municipality = tags.get("addr:municipality");
        address.postcode = tags.get("addr:postcode");
        address.node = node(xmlElmt);
        for (var p : paths) {
            if (p.name.equals(tags.get("addr:street"))) {
                address.street = p;
                break;
            }
        }
        return address;
    }

    public static MapBuilding building(Element xmlElmt, Map<String, MapNode> nodes) {
        MapBuilding building = new MapBuilding();

        building.id = xmlElmt.id();
        for (var nd : xmlElmt.getElementsByTag("nd")) {
            if (nodes.containsKey(nd.attributes().get("ref"))) {
                building.addNode(nodes.get(nd.attributes().get("ref")));
            }
        }

        return building;
    }

    public static MapPath path(Element xmlElmt, Map<String, MapNode> nodes) {
        MapPath path = new MapPath();

        var tags = MapData.extractTags(xmlElmt);
        path.id = xmlElmt.id();
        path.name = tags.containsKey("name") ? tags.get("name") : "unnamed";
        path.type = tags.containsKey("highway") ? tags.get("highway") : "unknown";
        try {
            path.maxSpeed = Double.valueOf(tags.get("maxspeed"));
        } catch (Exception e) {
            path.maxSpeed = 5;
        }

        for (var nd : xmlElmt.getElementsByTag("nd")) {
            if (nodes.containsKey(nd.attributes().get("ref"))) {
                path.addNode(nodes.get(nd.attributes().get("ref")));
            }
        }

        return path;
    }

    public static MapWay way(Element xmlElmt) {
        MapWay way = new MapWay();

        return way;
    }

}
