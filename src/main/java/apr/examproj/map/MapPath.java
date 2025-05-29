package apr.examproj.map;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import apr.examproj.config.ApplicationConfig;
import apr.examproj.gui.GUIUtils;
import apr.examproj.gui.IGUIMapElement;
import apr.examproj.gui.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

/**
 * MapWay
 */
public class MapPath implements IGUIMapElement {

    public String id;
    public String name;
    public String type;
    public double maxSpeed;

    List<MapNode> nodes = new ArrayList<>();

    public MapPath() {
    }

    public MapPath(String id, String name, String type, double maxSpeed) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.maxSpeed = maxSpeed;
    }

    public void addNode(MapNode node) {
        nodes.add(node);
    }

    public MapPath attachAddress(MapAddress addr) {
        addr.street = this;
        var closestNode = addr.node.findClosest(nodes);
        var newNode = new MapNode();
        MapPath newPath = new MapPath(UUID.randomUUID().toString(),
                "unnamed", "footpath",
                ApplicationConfig.getWalkingSpeed());
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i) == closestNode) {
                var index = i + 1 < nodes.size() ? (i + 1) : (i - 1);
                var secondClosestNode = nodes.get(index);
                var middlePoint = closestNode.middlePoint(secondClosestNode);
                newNode.id = UUID.randomUUID().toString();
                newNode.lat = middlePoint.x;
                newNode.lon = middlePoint.y;
                nodes.add(i + 1 < nodes.size() ? i + 1 : i, newNode);
                newPath.addNode(addr.node);
                newPath.addNode(newNode);
                break;
            }
        }
        return newPath;
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        Polyline line = new Polyline(GUIUtils.mapNodesToCoordArray(bounds, renderPane, nodes));
        line.setId("street-map__path");
        renderPane.getChildren().add(line);
        Tooltip.setTooltip(line, toString(),
                String.format("type: %s%nmax speed: %.1fkm/hr", type, maxSpeed),
                "id: " + id);
        for (var node : nodes) {
            node.draw(bounds, renderPane);
        }
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }

}
