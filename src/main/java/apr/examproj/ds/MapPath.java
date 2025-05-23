package apr.examproj.ds;

import java.util.ArrayList;
import java.util.List;

import apr.examproj.geom.MapBounds;
import apr.examproj.geom.MapNode;
import apr.examproj.gui.GUIMapElement;
import javafx.scene.layout.Pane;

/**
 * MapPath
 */
public class MapPath implements GUIMapElement {

    List<MapNode> nodes;

    public MapPath() {
        nodes = new ArrayList<>();
    }

    public List<MapNode> getNodes() {
        return List.of(nodes.toArray(MapNode[]::new));
    }

    public void addNode(MapNode node) {
        nodes.add(node);
    }

    public void removeNode(MapNode node) {
        nodes.remove(node);
    }

    @Override
    public javafx.scene.Node guify(Pane parentPane, MapBounds mapBounds) {
        return new Pane();
    }

}
