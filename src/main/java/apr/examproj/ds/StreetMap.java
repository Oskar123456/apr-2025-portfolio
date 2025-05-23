package apr.examproj.ds;

import java.util.ArrayList;
import java.util.List;

import apr.examproj.geom.MapBounds;
import apr.examproj.geom.MapBuilding;
import apr.examproj.geom.MapNode;
import apr.examproj.geom.MapWay;
import apr.examproj.gui.GUIFactory;
import apr.examproj.osm.MapData;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Map
 */
public class StreetMap {

    MapBounds bounds;
    List<MapNode> nodes = new ArrayList<>();
    List<MapNode> travelNodes = new ArrayList<>();
    List<MapWay> streets = new ArrayList<>();
    List<MapBuilding> buildings = new ArrayList<>();
    List<MapAddress> addresses = new ArrayList<>();
    MapAddress srcAddr, destAddr;

    Pane layout, title, content;

    public StreetMap(MapData mapData) {
        bounds = new MapBounds(mapData.getBounds());
        for (var node : mapData.getNodes()) {
            nodes.add(new MapNode(node));
            var tags = MapData.extractTags(node);
            if (tags.containsKey("addr:street")) {
                addresses.add(new MapAddress(node));
            } else {
                nodes.add(new MapNode(node));
            }

            // System.out.println("[NODES] StreetMap.StreetMap(): added " +
            // nodes.get(nodes.size() - 1));
        }
        for (var street : mapData.getStreets()) {
            var tags = MapData.extractTags(street);
            var way = new MapWay(street.id(), tags.get("name"), tags.get("highway"));
            for (var nd : street.getElementsByTag("nd")) {
                for (int i = 0; i < nodes.size(); i++) {
                    var node = nodes.get(i);
                    if (nd.attributes().get("ref").equals(node.id)) {
                        way.addNode(node);
                        travelNodes.add(node);
                    }
                }
            }
            streets.add(way);
            for (var addr : addresses) {
                if (addr.street.equals(way.name)) {
                    addr.mapStreet = way;
                }
            }
            // System.out.println("[STREETS] StreetMap.StreetMap(): added " +
            // streets.get(streets.size() - 1));
        }
        for (var building : mapData.getBuildings()) {
            var tags = MapData.extractTags(building);
            if (tags.get("building").equals("yes") || tags.get("building").equals("detached")) {
                var mapBuilding = new MapBuilding();
                mapBuilding.id = building.id();
                for (var nd : building.getElementsByTag("nd")) {
                    for (int i = 0; i < nodes.size(); i++) {
                        var node = nodes.get(i);
                        if (nd.attributes().get("ref").equals(node.id)) {
                            mapBuilding.addNode(node);
                            // nodes.remove(i);
                        }
                    }
                }
                buildings.add(mapBuilding);
                // System.out.println("[BUILDINGS] StreetMap.StreetMap(): added " +
                // buildings.get(buildings.size() - 1));
            }
        }
    }

    public void setRenderTarget(Pane parentPane) {
        System.out.println(
                "StreetMap.guify() : " + parentPane + " " + parentPane.getWidth() + " " + parentPane.getHeight());

        layout = GUIFactory.defaultChildVBox(parentPane, "street-map__street-map-layout");

        title = new HBox();
        title.setId("street-map__street-map-title");
        title.prefWidthProperty().bind(layout.widthProperty());
        title.prefHeight(50);
        title.getChildren().add(new Text("STREET MAP VIEW OF " + bounds));

        content = new Pane();
        content.setId("street-map__street-map-content");
        content.prefWidthProperty().bind(layout.widthProperty());
        content.prefHeightProperty().bind(layout.heightProperty().subtract(title.heightProperty()));
        content.heightProperty().addListener(e -> draw());
        content.widthProperty().addListener(e -> draw());

        layout.getChildren().addAll(title, content);

        draw();
    }

    void draw() {
        content.getChildren().clear();
        buildings.forEach(s -> s.guify(content, bounds));
        streets.forEach(s -> s.guify(content, bounds));
        travelNodes.forEach(s -> s.guify(content, bounds));
        addresses.forEach(s -> {
            s.guify(content, bounds);
            for (var child : s.guiNode.getChildren()) {
                child.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                    if (e.isShiftDown()) {
                        changeDest(s);
                    } else {
                        changeSrc(s);
                    }
                });
            }
        });
    }

    void changeSrc(MapAddress addr) {
        addresses.forEach(a -> a.isSrc = false);
        addr.setAsSrc();
        draw();
    }

    void changeDest(MapAddress addr) {
        addresses.forEach(a -> a.isDest = false);
        addr.setAsDest();
        draw();
    }

    @Override
    public String toString() {
        return String.format("%s[bounds: %s, nodes: %d, streets: %d, buildings: %d]",
                getClass().getSimpleName(), bounds.toString(), nodes.size(), streets.size(), buildings.size());
    }

}
