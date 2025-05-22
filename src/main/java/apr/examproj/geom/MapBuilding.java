package apr.examproj.geom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jsoup.nodes.Element;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;

/**
 * Building
 */
public class MapBuilding extends MapPolygon {

    public String id;

    public MapBuilding() {
        this.nodes = new ArrayList<>();
    }

    public MapBuilding(MapNode... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));
    }

    public MapBuilding(Element xmlElmt) {
        this.id = xmlElmt.id();
        this.nodes = new ArrayList<>();
        for (var nd : xmlElmt.getElementsByTag("nd")) {
            for (var node : nodes) {
                if (nd.attributes().get("ref").equals(node.id)) {
                    nodes.add(node);
                    break;
                }
            }
        }
    }

    public void findNodes(List<MapNode> nodes) {

    }

    @Override
    public javafx.scene.Node guify(Pane parentPane, MapBounds mapBounds) {
        Polygon poly = GeomFactory.polygon(mapBounds,
                parentPane.getWidth(),
                parentPane.getHeight(),
                nodes, "street-map__street-map-building");

        parentPane.getChildren().add(poly);
        return poly;
    }

}
