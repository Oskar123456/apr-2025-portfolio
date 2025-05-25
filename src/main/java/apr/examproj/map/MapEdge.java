package apr.examproj.map;

import apr.examproj.gui.IGUIMapElement;
import javafx.scene.layout.Pane;

/**
 * MapEdge
 */
public class MapEdge implements IGUIMapElement {

    String name;
    String description;
    double dist;

    public MapEdge() {
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
    }

}
