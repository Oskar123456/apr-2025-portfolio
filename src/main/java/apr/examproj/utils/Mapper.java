package apr.examproj.utils;

import apr.examproj.ds.Graph;
import apr.examproj.ds.GraphEdge;
import apr.examproj.ds.GraphNode;
import apr.examproj.enums.TransportationMode;
import apr.examproj.map.MapNode;
import apr.examproj.map.StreetMap;

/**
 * Mapper
 */
public class Mapper {

    public static Graph<MapNode> StreetMapToGraph(StreetMap map, TransportationMode transportationMode) {
        Graph<MapNode> graph = new Graph<>();

        for (var edge : map.getAllEdges()) {
            GraphNode<MapNode> srcNode = new GraphNode<MapNode>(edge.src);
            GraphNode<MapNode> destNode = new GraphNode<MapNode>(edge.dest);
            GraphEdge<MapNode> newEdge = new GraphEdge<>(srcNode, destNode, edge.getTravelTime(transportationMode));
            graph.addEdge(newEdge);
            System.out.println("Mapper.StreetMapToGraph(): " + newEdge);
        }

        return graph;
    }

}
