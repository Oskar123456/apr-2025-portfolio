package apr.examproj.geom;

import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Polygon;

/**
 * Street
 */
public class MapWay extends MapLine {

    public String id;
    public String name;
    public String type;
    public double maxSpeed;

    public MapWay() {
        super();
    }

    public MapWay(String id, String name, String type) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public MapWay(String id, String name, String type, MapNode... nodes) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        for (var n : nodes) {
            addNode(n);
        }
    }

    public String toString() {
        return String.format("Way[id: %s, name: %s, type: %s, maxSpeed: %f, nodes: %s]",
                id, name, type, maxSpeed, String.join(",", nodes.stream().map(n -> n.toString()).toList()));
    }

    @Override
    public javafx.scene.Node guify(Region parentRegion, MapBounds mapBounds) {
        Polygon poly = new Polygon();
        return poly;
    }

}
