package apr.examproj.ds;

import java.util.ArrayList;
import java.util.List;

import apr.examproj.geom.MapBounds;
import apr.examproj.geom.MapBuilding;
import apr.examproj.geom.MapNode;
import apr.examproj.geom.MapWay;
import apr.examproj.gui.GUIMapElement;
import apr.examproj.osm.MapData;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * Map
 */
public class StreetMap {

    MapBounds bounds;
    List<MapNode> nodes = new ArrayList<>();
    List<MapWay> streets = new ArrayList<>();
    List<MapBuilding> buildings = new ArrayList<>();

    public StreetMap(MapData mapData) {
        bounds = new MapBounds(mapData.getBounds());
        for (var node : mapData.getNodes()) {
            nodes.add(new MapNode(node));
        }
        for (var street : mapData.getStreets()) {
            var tags = MapData.extractTags(street);
            var way = new MapWay(street.id(), tags.get("name"), tags.get("highway"));
            for (var nd : street.getElementsByTag("nd")) {
                for (var node : nodes) {
                    if (nd.id().equals(node.id)) {
                        way.addNode(node);
                        break;
                    }
                }
            }
            streets.add(way);
        }
        for (var building : mapData.getBuildings()) {
            var tags = MapData.extractTags(building);
            if (tags.get("building").equals("yes")) {
                buildings.add(new MapBuilding(building));
            }
        }
    }

    public javafx.scene.Node guify(Region parentRegion) {
        return new Pane();
    }

    @Override
    public String toString() {
        return String.format("%s[bounds: %s, nodes: %d, streets: %d, buildings: %d]",
                getClass().getSimpleName(), bounds.toString(), nodes.size(), streets.size(), buildings.size());
    }

}
