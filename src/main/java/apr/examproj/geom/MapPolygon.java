package apr.examproj.geom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import apr.examproj.gui.GUIMapElement;

/**
 * Polygon
 */
public abstract class MapPolygon implements GUIMapElement {

    public List<MapNode> nodes;

    public MapPolygon() {
        nodes = new ArrayList<>();
    }

    public MapPolygon(MapNode... nodes) {
        this.nodes = new ArrayList<>(Arrays.asList(nodes));
    }

    public void addNode(MapNode node) {
        nodes.add(node);
    }

}
