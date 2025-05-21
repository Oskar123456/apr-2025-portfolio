package apr.examproj.ds;

import java.util.ArrayList;
import java.util.List;

import apr.examproj.geom.MapBounds;
import apr.examproj.geom.MapBuilding;
import apr.examproj.geom.MapNode;
import apr.examproj.geom.MapWay;
import apr.examproj.gui.GUIMapElement;
import apr.examproj.gui.GuiFactory;
import apr.examproj.osm.MapData;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

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
            // System.out.println("[NODES] StreetMap.StreetMap(): added " +
            // nodes.get(nodes.size() - 1));
        }
        for (var street : mapData.getStreets()) {
            var tags = MapData.extractTags(street);
            var way = new MapWay(street.id(), tags.get("name"), tags.get("highway"));
            for (var nd : street.getElementsByTag("nd")) {
                for (var node : nodes) {
                    if (nd.attributes().get("ref").equals(node.id)) {
                        way.addNode(node);
                    }
                }
            }
            streets.add(way);
            // System.out.println("[STREETS] StreetMap.StreetMap(): added " +
            // streets.get(streets.size() - 1));
        }
        for (var building : mapData.getBuildings()) {
            var tags = MapData.extractTags(building);
            if (tags.get("building").equals("yes")) {
                buildings.add(new MapBuilding(building));
                // System.out.println("[BUILDINGS] StreetMap.StreetMap(): added " +
                // buildings.get(buildings.size() - 1));
            }
        }
    }

    public javafx.scene.Node guify(Pane parentPane) {
        System.out.println(
                "StreetMap.guify() : " + parentPane + " " + parentPane.getWidth() + " " + parentPane.getHeight());

        parentPane.getChildren().clear();

        VBox content = GuiFactory.defaultChildVBox(parentPane, "street-map__content");

        // HBox titleBox = GuiFactory.defaultChildHBox(content,
        // "street-map__title-box");
        // Pane main = GuiFactory.defaultChildPane(content, "street-map__main");

        streets.forEach(s -> s.guify(parentPane, bounds));

        return content;
    }

    @Override
    public String toString() {
        return String.format("%s[bounds: %s, nodes: %d, streets: %d, buildings: %d]",
                getClass().getSimpleName(), bounds.toString(), nodes.size(), streets.size(), buildings.size());
    }

}
