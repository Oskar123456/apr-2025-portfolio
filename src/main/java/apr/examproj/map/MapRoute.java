package apr.examproj.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import apr.examproj.ds.Graph;
import apr.examproj.gui.IGUIMapElement;
import apr.examproj.utils.Stringify;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * MapRoute
 */
public class MapRoute implements IGUIMapElement {

    MapAddress src, dest;
    Set<MapNode> nodes = new HashSet<>();
    List<MapEdge> edges = new ArrayList<>();

    public Graph<MapNode> graph;
    public double hours, dist;

    public MapRoute(Graph<MapNode> graph, MapAddress src, MapAddress dest, List<MapEdge> edges) {
        this.graph = graph;
        this.src = src;
        this.dest = dest;
        this.edges = edges;
        edges.forEach(e -> {
            nodes.add(e.src);
            nodes.add(e.dest);
        });
        dist = edges.stream().map(e -> e.dist).reduce(0D, (acc, value) -> acc += value);
        hours = edges.stream().map(e -> e.getTravelTime()).reduce(0D, (acc, value) -> acc += value);
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        edges.forEach(e -> e.draw(renderPane));
        // nodes.forEach(n -> {
        // if (n != src.node && n != dest.node) {
        // n.draw(bounds, renderPane);
        // }
        // });
    }

    public void draw(Pane renderPane) {
        edges.forEach(e -> e.draw(renderPane));
        // nodes.forEach(n -> {
        // if (n != src.node && n != dest.node) {
        // n.draw(bounds, renderPane);
        // }
        // });
    }

    public List<Node> drawNodes() {
        return edges.stream().map(e -> e.drawNode()).toList();
    }

    public String getDescription() {
        return String.format("Route from %s to %s:", src.toString(), dest.toString());
    }

    @Override
    public String toString() {
        return Stringify.toString(this);
    }

}
