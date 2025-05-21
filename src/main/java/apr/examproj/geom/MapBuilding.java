package apr.examproj.geom;

import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.nodes.Element;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

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
                if (nd.id().equals(node.id)) {
                    nodes.add(node);
                    break;
                }
            }
        }
    }

    @Override
    public javafx.scene.Node guify(Pane parentPane, MapBounds mapBounds) {
        return new Pane();
    }

}
