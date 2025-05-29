package apr.examproj.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import apr.examproj.gui.IGUIMapElement;
import apr.examproj.utils.Stringify;
import javafx.scene.layout.Pane;

/**
 * MapRoute
 */
public class MapRoute implements IGUIMapElement {

    MapAddress src, dest;
    Set<MapNode> nodes = new HashSet<>();
    List<MapEdge> edges = new ArrayList<>();

    public double hours, dist;

    public MapRoute(MapAddress src, MapAddress dest, List<MapEdge> edges) {
        this.src = src;
        this.dest = dest;
        this.edges = edges;
        edges.forEach(e -> {
            nodes.add(e.src);
            nodes.add(e.dest);
        });
        dist = edges.stream().map(e -> e.dist).reduce(0D, (acc, value) -> acc += value);
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        edges.forEach(e -> e.draw(bounds, renderPane));
        nodes.forEach(n -> n.draw(bounds, renderPane));
    }

    @Override
    public String toString() {
        return Stringify.toString(this);
    }

}
