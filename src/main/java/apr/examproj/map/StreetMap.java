package apr.examproj.map;

import java.util.ArrayList;
import java.util.List;

import apr.examproj.gui.IGUIMapElement;
import apr.examproj.osm.MapData;
import apr.examproj.utils.Stringify;
import javafx.scene.Node;
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
        nodes = mapData.getNodes().stream().map(MapFactory::node).toList();
        buildings = mapData.getBuildings().stream().map(MapFactory::building).toList();
        paths = mapData.getPaths().stream().map(p -> MapFactory.path(p, nodes)).toList();
        ways = mapData.getWays().stream().map(MapFactory::way).toList();
        addresses = mapData.getAddresses().stream().map(MapFactory::address).toList();
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

        addresses.forEach(a -> a.draw(bounds, renderPane));
        paths.forEach(p -> p.draw(bounds, renderPane));
    }

    @Override
    public String toString() {
        return Stringify.toString(this);
    }

    @Override
    public Node tooltip(Pane parentPane) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tooltip'");
    }

    @Override
    public void setHoverTooltipTarget(Pane tooltipTarget) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setHoverTooltipTarget'");
    }

}
