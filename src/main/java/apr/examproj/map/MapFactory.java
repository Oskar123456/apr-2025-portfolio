package apr.examproj.map;

import org.jsoup.nodes.Element;

import apr.examproj.osm.MapData;

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
        address.nodeId = xmlElmt.id();
        address.city = tags.get("addr:city");
        address.country = tags.get("addr:country");
        address.housenumber = tags.get("addr:housenumber");
        address.municipality = tags.get("addr:municipality");
        address.postcode = tags.get("addr:postcode");
        address.street = tags.get("addr:street");
        return address;
    }

}
