package apr.examproj.map;

import java.util.ArrayList;
import java.util.List;

import apr.examproj.gui.IGUIMapElement;
import apr.examproj.osm.MapData;
import apr.examproj.utils.Stringify;
import javafx.scene.layout.Pane;

/**
 * StreetMap
 */
public class StreetMap implements IGUIMapElement {

    MapBounds bounds;
    List<MapNode> nodes = new ArrayList<>();
    List<MapWay> ways = new ArrayList<>(); // misc
    List<MapPath> paths = new ArrayList<>(); // pathable
    List<MapEdge> edges = new ArrayList<>(); // granular paths
    List<MapBuilding> buildings = new ArrayList<>();
    List<MapAddress> addresses = new ArrayList<>();

    public StreetMap(MapData mapData) {
        bounds = MapFactory.bounds(mapData.getBounds());
        nodes = new ArrayList<>(mapData.getNodes().stream().map(MapFactory::node).toList());
        buildings = new ArrayList<>(mapData.getBuildings().stream().map(b -> MapFactory.building(b, nodes)).toList());
        paths = new ArrayList<>(mapData.getPaths().stream().map(p -> MapFactory.path(p, nodes)).toList());
        ways = new ArrayList<>(mapData.getWays().stream().map(MapFactory::way).toList());
        addresses = new ArrayList<>(mapData.getAddresses().stream().map(a -> MapFactory.address(a, paths)).toList());
        linkAddresses(); // TODO : change to nearest point, and fix responsibility, a litte weird call
                         // hierarchy atm.
    }

    private void linkAddresses() {
        List<MapPath> newPaths = new ArrayList<>();
        for (var addr : addresses) {
            for (var p : paths) {
                if (addr.street == p) {
                    newPaths.add(p.attachAddress(addr));
                }
            }
        }
        paths.addAll(newPaths);
    }

    public void setRenderTarget(Pane renderPane) {
        System.out.println("StreetMap.setRenderTarget()");
        renderPane.widthProperty().addListener((e) -> draw(bounds, renderPane));
        renderPane.heightProperty().addListener((e) -> draw(bounds, renderPane));
        draw(bounds, renderPane);
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        renderPane.getChildren().clear();

        nodes.forEach(n -> n.draw(bounds, renderPane));
        paths.forEach(p -> p.draw(bounds, renderPane));
        buildings.forEach(b -> b.draw(bounds, renderPane));
        addresses.forEach(a -> a.draw(bounds, renderPane));
    }

    @Override
    public String toString() {
        return Stringify.toString(this);
    }

}
