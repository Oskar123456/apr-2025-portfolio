package apr.examproj.map;

import java.util.ArrayList;
import java.util.List;

import apr.examproj.gui.GUIUtils;
import apr.examproj.gui.IGUIMapElement;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

/**
 * MapBuilding
 */
public class MapBuilding implements IGUIMapElement {

    public String id;
    List<MapNode> nodes;

    public MapBuilding() {
    }

    public void addNode(MapNode node) {
        if (this.nodes == null) {
            this.nodes = new ArrayList<>();
        }
        nodes.add(node);
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        Polygon line = new Polygon(GUIUtils.mapNodesToCoordArray(bounds, renderPane, nodes));
        line.setId("street-map__building");
        renderPane.getChildren().add(line);
    }

}
