// package apr.examproj.geom;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import org.apache.commons.lang3.ArrayUtils;
//
// import apr.datastructures.graph.Point2D;
// import javafx.scene.layout.Pane;
// import javafx.scene.layout.Region;
// import javafx.scene.paint.Color;
// import javafx.scene.shape.Polygon;
// import javafx.scene.shape.Polyline;
//
// /**
// * Street
// */
// public class MapWay extends MapLine {
//
// public String id;
// public String name;
// public String type;
// public double maxSpeed;
//
// public MapWay() {
// super();
// }
//
// public MapWay(String id, String name, String type) {
// super();
// this.id = id;
// this.name = name;
// this.type = type;
// }
//
// public MapWay(String id, String name, String type, MapNode... nodes) {
// super();
// this.id = id;
// this.name = name;
// this.type = type;
// for (var n : nodes) {
// addNode(n);
// }
// }
//
// public String toString() {
// return String.format("Way[id: %s, name: %s, type: %s, maxSpeed: %f, nodes:
// %s]",
// id, name, type, maxSpeed, String.join(",", nodes.stream().map(n ->
// n.toString()).toList()));
// }
//
// @Override
// public javafx.scene.Node guify(Pane parentPane, MapBounds mapBounds) {
// Polyline poly = GeomFactory.polyline(mapBounds,
// parentPane.getWidth(),
// parentPane.getHeight(),
// nodes, "street-map__street-map-way");
// parentPane.getChildren().add(poly);
//
// // System.out.println(
// // "MapWay.guify() : parent: " + parentPane + " " + parentPane.getWidth() + "
// "
// // + parentPane.getHeight());
// // System.out.println("MapWay.guify() : " + poly.toString());
//
// return poly;
// }
//
// }
