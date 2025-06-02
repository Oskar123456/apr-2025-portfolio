package apr.examproj.map;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import apr.datastructures.graph.Point2D;
import apr.examproj.config.ApplicationConfig;
import apr.examproj.gui.GUIUtils;
import apr.examproj.gui.IGUIMapElement;
import apr.examproj.gui.Tooltip;
import apr.examproj.utils.Geometry;
import javafx.scene.layout.Pane;
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

        Point2D addrPoint = addr.node.getPos();
        Point2D closestPoint = null;
        int idx = 0;
        for (int i = 1; i < nodes.size(); i++) {
            var n1 = nodes.get(i - 1);
            var n2 = nodes.get(i);
            var p = Geometry.projection(addrPoint, n1.getPos(), n2.getPos());
            if (closestPoint == null || p.dist(addrPoint) < closestPoint.dist(addrPoint)) {
                closestPoint = p;
                idx = i;
            }
        }
        if (closestPoint == null) {
            return null;
        }

        var newNode = new MapNode(UUID.randomUUID().toString(), closestPoint.x, closestPoint.y);
        MapPath newPath = new MapPath(UUID.randomUUID().toString(),
                "unnamed", "footpath", ApplicationConfig.getWalkingSpeed());
        newPath.addNode(addr.node);
        newPath.addNode(newNode);

        List<MapNode> newNodes = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (i == idx) {
                newNodes.add(newNode);
            }
            newNodes.add(nodes.get(i));
        }
        nodes = newNodes;

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

    public void drawSubtle(MapBounds bounds, Pane renderPane) {
        // Polyline line = new Polyline(GUIUtils.mapNodesToCoordArray(bounds,
        // renderPane, nodes));
        // line.setId("street-map__path-subtle");
        // renderPane.getChildren().add(line);
        // Tooltip.setTooltip(line, toString(),
        // String.format("type: %s%nmax speed: %.1fkm/hr", type, maxSpeed),
        // "id: " + id);
        // for (var node : nodes) {
        // node.draw(bounds, renderPane);
        // }
    }

    public List<MapNode> getNodes() {
        return nodes;
    }

    public String getDescription() {
        return String.format("type: %s%nmax speed: %.1fkm/hr", type, maxSpeed);
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }

}
