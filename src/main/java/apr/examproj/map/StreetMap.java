package apr.examproj.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import apr.examproj.alg.PathFinder;
import apr.examproj.application.StreetMapApp;
import apr.examproj.ds.Graph;
import apr.examproj.ds.GraphEdge;
import apr.examproj.ds.GraphNode;
import apr.examproj.enums.TransportationMode;
import apr.examproj.gui.IGUIMapElement;
import apr.examproj.osm.MapData;
import apr.examproj.utils.Stringify;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * StreetMap
 */
public class StreetMap implements IGUIMapElement {

    // MapBounds bounds;
    List<MapNode> nodes = new ArrayList<>();
    List<MapWay> ways = new ArrayList<>(); // misc
    List<MapPath> paths = new ArrayList<>(); // pathable
    List<MapPath> linkPaths = new ArrayList<>(); // pathable, links unreachable nodes by footpath, should not be drawn
    List<MapEdge> edges = new ArrayList<>(); // granular paths
    List<MapBuilding> buildings = new ArrayList<>();
    List<MapAddress> addresses = new ArrayList<>();

    public StreetMap(MapData mapData) {
        // bounds = MapFactory.bounds(mapData.getBounds());
        nodes = new ArrayList<>(mapData.getNodes().stream().map(MapFactory::node).toList());
        buildings = new ArrayList<>(mapData.getBuildings().stream().map(b -> MapFactory.building(b, nodes)).toList());
        paths = new ArrayList<>(mapData.getPaths().stream().map(p -> MapFactory.path(p, nodes)).toList());
        ways = new ArrayList<>(mapData.getWays().stream().map(MapFactory::way).toList());
        addresses = new ArrayList<>(mapData.getAddresses().stream().map(a -> MapFactory.address(a, paths)).toList());
        linkAddresses(); // TODO : change to nearest point, and fix responsibility, a litte weird call
                         // hierarchy atm.
    }

    public List<MapEdge> getAllEdges() {
        if (edges == null || edges.size() < 1) {
            makeEdges();
        }
        return edges;
    }

    private void makeEdges() {
        List<MapEdge> edges = new ArrayList<>();

        for (var p : paths) {
            for (int i = 1; i < p.nodes.size(); i++) {
                edges.add(new MapEdge(p.nodes.get(i - 1), p.nodes.get(i), p));
                edges.add(new MapEdge(p.nodes.get(i), p.nodes.get(i - 1), p));
            }
        }

        for (var p : linkPaths) {
            edges.add(new MapEdge(p.nodes.get(0), p.nodes.get(1), p));
            edges.add(new MapEdge(p.nodes.get(1), p.nodes.get(0), p));
        }

        this.edges = edges;
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
        linkPaths.addAll(newPaths);
    }

    public void setRenderTarget(Pane renderPane) {
        // System.out.println("StreetMap.setRenderTarget()");
        // draw(bounds, renderPane);
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        paths.forEach(p -> p.draw(bounds, renderPane));
        buildings.forEach(b -> b.draw(bounds, renderPane));
        addresses.forEach(a -> a.draw(bounds, renderPane));
        // getAllEdges().forEach(a -> a.draw(bounds, renderPane));
    }

    public Graph<MapNode> toGraph(TransportationMode transportationMode, MapNode src, MapNode dest) {
        Graph<MapNode> graph = new Graph<>();

        for (var edge : getAllEdges()) {
            GraphNode<MapNode> srcNode = new GraphNode<MapNode>(edge.src, edge.src.lat, edge.src.lon);
            GraphNode<MapNode> destNode = new GraphNode<MapNode>(edge.dest, edge.src.lat, edge.src.lon);
            GraphEdge<MapNode> newEdge = new GraphEdge<>(srcNode, destNode, edge.getTravelTime(transportationMode));
            graph.addEdge(newEdge);
            if (edge.src == src) {
                graph.setStart(srcNode);
            }
            if (edge.dest == dest) {
                graph.setDestination(destNode);
            }
        }

        return graph;
    }

    public MapRoute getRoute(TransportationMode transportationMode, PathFinder<MapNode> pathFinder, MapAddress src,
            MapAddress dest)
            throws Exception {

        Map<String, GraphNode<MapNode>> nodeMap = new HashMap<>();
        Map<GraphEdge<MapNode>, MapEdge> edgeMap = new HashMap<>();
        Graph<MapNode> graph = new Graph<>();

        for (var edge : getAllEdges()) {
            if (!nodeMap.containsKey(edge.src.id)) {
                nodeMap.put(edge.src.id, new GraphNode<MapNode>(edge.src, edge.src.lat, edge.src.lon));
            }
            if (!nodeMap.containsKey(edge.dest.id)) {
                nodeMap.put(edge.dest.id, new GraphNode<MapNode>(edge.dest, edge.dest.lat, edge.dest.lon));
            }
            GraphNode<MapNode> srcNode = nodeMap.get(edge.src.id);
            GraphNode<MapNode> destNode = nodeMap.get(edge.dest.id);
            GraphEdge<MapNode> newEdge = new GraphEdge<>(srcNode, destNode, edge.getTravelTime(transportationMode));
            graph.addEdge(newEdge);
            edgeMap.put(newEdge, edge);
            if (edge.src == src.node) {
                graph.setStart(srcNode);
            }
            if (edge.dest == dest.node) {
                graph.setDestination(destNode);
            }
        }

        pathFinder.search(graph);
        var pathNodes = graph.getPathNodes();
        var pathEdges = graph.getPathEdges();

        List<MapEdge> routeEdges = pathEdges.stream().map(e -> edgeMap.get(e)).toList();

        return new MapRoute(src, dest, routeEdges);
    }

    @Override
    public String toString() {
        return Stringify.toString(this);
    }

}
