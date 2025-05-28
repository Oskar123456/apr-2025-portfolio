package apr.examproj.map;

import java.util.List;

import apr.examproj.gui.IGUIMapElement;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;

/**
 * MapRoute
 */
public class MapRoute implements IGUIMapElement {

    List<MapNode> nodes;
    List<MapEdge> edges;

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
    }

}
