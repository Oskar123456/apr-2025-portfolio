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
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Map
 */
public class StreetMap {

    MapBounds bounds;
    List<MapNode> nodes = new ArrayList<>();
    List<MapWay> streets = new ArrayList<>();
    List<MapBuilding> buildings = new ArrayList<>();

    Pane layout, title, content;

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

    public void setRenderTarget(Pane parentPane) {
        System.out.println(
                "StreetMap.guify() : " + parentPane + " " + parentPane.getWidth() + " " + parentPane.getHeight());

        if (layout == null) {
            layout = GuiFactory.defaultChildVBox(parentPane, "street-map__street-map-layout");
            title = GuiFactory.defaultChildHBox(layout, "street-map__street-map-title");
            title.getChildren().add(new Text("STREET MAP VIEW OF " + bounds));
            content = GuiFactory.defaultChildPane(layout, "street-map__street-map-content");
            content.heightProperty().addListener(e -> draw());
            content.widthProperty().addListener(e -> draw());
        }

        draw();
    }

    void draw() {
        content.getChildren().clear();
        streets.forEach(s -> s.guify(content, bounds));
    }

    @Override
    public String toString() {
        return String.format("%s[bounds: %s, nodes: %d, streets: %d, buildings: %d]",
                getClass().getSimpleName(), bounds.toString(), nodes.size(), streets.size(), buildings.size());
    }

}
