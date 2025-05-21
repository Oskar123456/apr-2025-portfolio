package apr.examproj.geom;

import java.util.List;

import apr.examproj.gui.GUIMapElement;

/**
 * Line
 */
public abstract class MapLine implements GUIMapElement {

    public List<MapNode> nodes;

    public void addNode(MapNode node) {
        nodes.add(node);
    }

}
