package apr.examproj.osm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

/**
 * MapData
 */
public class MapData {

    Element bounds;
    List<Element> nodes = new ArrayList<>();
    List<Element> ways = new ArrayList<>();
    List<Element> paths = new ArrayList<>();
    List<Element> buildings = new ArrayList<>();
    List<Element> addresses = new ArrayList<>();

    public MapData(String data) {
        Document xmlDoc = Jsoup.parse(data, Parser.xmlParser());

        bounds = xmlDoc.getElementsByTag("bounds").get(0);

        for (var e : xmlDoc.getElementsByTag("node")) {
            nodes.add(e);
            var eTags = extractTags(e);
            if (eTags.containsKey("addr:street")) {
                addresses.add(e);
            }
        }

        for (var e : xmlDoc.getElementsByTag("way")) {
            var eTags = extractTags(e);
            if (eTags.containsKey("highway")) {
                paths.add(e);
            } else if (eTags.containsKey("building")) {
                buildings.add(e);
            } else {
                ways.add(e);
            }
        }
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
        return String.format("%s[bounds: %s, nodes: %d, paths: %d, buildings: %d]",
                getClass().getSimpleName(), bounds.toString(), nodes.size(), paths.size(), buildings.size());
    }

    public Element getBounds() {
        return bounds;
    }

    public List<Element> getNodes() {
        return nodes;
    }

    public List<Element> getBuildings() {
        return buildings;
    }

    public List<Element> getPaths() {
        return paths;
    }

    public List<Element> getWays() {
        return ways;
    }

    public List<Element> getAddresses() {
        return addresses;
    }

}
