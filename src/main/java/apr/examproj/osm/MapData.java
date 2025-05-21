package apr.examproj.osm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import apr.examproj.geom.MapNode;

/**
 * MapData
 */
public class MapData {

    Element bounds;
    List<Element> nodes = new ArrayList<>();
    List<Element> ways = new ArrayList<>();
    List<Element> streets = new ArrayList<>();
    List<Element> buildings = new ArrayList<>();

    public MapData(String data) {
        Document xmlDoc = Jsoup.parse(data, Parser.xmlParser());

        bounds = xmlDoc.getElementsByTag("bounds").get(0);

        for (var e : xmlDoc.getElementsByTag("node")) {
            nodes.add(e);
        }

        for (var e : xmlDoc.getElementsByTag("way")) {
            var eTags = extractTags(e);
            if (eTags.containsKey("highway")) {
                streets.add(e);
            } else if (eTags.containsKey("building")) {
                buildings.add(e);
            } else {
                ways.add(e);
            }
        }
    }

    public Element getBounds() {
        return bounds;
    }

    public List<Element> getNodes() {
        return nodes;
    }

    public List<Element> getStreets() {
        return streets;
    }

    public List<Element> getBuildings() {
        return buildings;
    }

    public static Map<String, String> extractTags(Element element) {
        Map<String, String> map = new HashMap<>();

        for (var t : element.getElementsByTag("tag")) {
            map.put(t.attributes().get("k"), t.attributes().get("v"));
        }

        return map;
    }

    @Override
    public String toString() {
        return String.format("%s[bounds: %s, nodes: %d, streets: %d, buildings: %d]",
                getClass().getSimpleName(), bounds.toString(), nodes.size(), streets.size(), buildings.size());
    }

}
