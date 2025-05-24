// package apr.examproj.ds;
//
// import org.eclipse.jetty.server.session.HouseKeeper;
// import org.jsoup.nodes.Element;
//
// import apr.examproj.geom.GeomFactory;
// import apr.examproj.geom.MapBounds;
// import apr.examproj.geom.MapNode;
// import apr.examproj.geom.MapWay;
// import apr.examproj.gui.GUIMapElement;
// import apr.examproj.osm.MapData;
// import javafx.scene.Node;
// import javafx.scene.layout.Pane;
// import javafx.scene.paint.Color;
// import javafx.scene.shape.Ellipse;
// import javafx.scene.text.Text;
//
// /**
// * Address
// */
// public class MapAddress implements GUIMapElement {
//
// String id;
// String city;
// String country;
// String municipality;
// String postcode;
// String street;
// String housenumber;
//
// public MapNode mapNode;
// public MapWay mapStreet;
// public boolean isSrc, isDest;
// public Pane guiNode;
//
// public MapAddress(Element xmlElmt) {
// this.mapNode = new MapNode(xmlElmt);
// var tags = MapData.extractTags(xmlElmt);
// city = tags.get("addr:city");
// country = tags.get("addr:country");
// housenumber = tags.get("addr:housenumber");
// municipality = tags.get("addr:municipality");
// postcode = tags.get("addr:postcode");
// street = tags.get("addr:street");
// id = tags.get("osak:identifier");
// }
//
// public void setAsDest() {
// isSrc = false;
// isDest = true;
// }
//
// public void setAsSrc() {
// isDest = false;
// isSrc = true;
// }
//
// public void clear() {
// isSrc = false;
// isDest = false;
// }
//
// @Override
// public Node guify(Pane parentPane, MapBounds mapBounds) {
// Pane container = GeomFactory.container(mapBounds,
// parentPane.getWidth(),
// parentPane.getHeight(),
// mapNode, "street-map__street-map-address-container");
//
// double r = 0.013;
// Ellipse dot = new Ellipse(r * parentPane.getWidth(), r *
// parentPane.getHeight());
//
// if (isSrc) {
// dot.setId("street-map__street-map-address-node-src");
// } else if (isDest) {
// dot.setId("street-map__street-map-address-node-dest");
// System.out.println("srrc:" + mapNode);
// } else {
// dot.setId("street-map__street-map-address-node");
// }
//
// Text txt = new Text(housenumber);
// txt.setId("street-map__street-map-address-text");
// txt.relocate(-(r / 2) * parentPane.getWidth(), -(r / 2) *
// parentPane.getHeight());
// txt.maxWidth(r * 1.75 * parentPane.getWidth());
// var pos = mapBounds.normalize(mapNode.lat, mapNode.lon);
//
// container.getChildren().add(dot);
// container.getChildren().add(txt);
// parentPane.getChildren().add(container);
// guiNode = container;
// return container;
// }
//
// }
