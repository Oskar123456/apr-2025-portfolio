package apr.examproj.map;

import java.util.List;

import apr.examproj.geom.MapNode;
import apr.examproj.gui.IGUIMapElement;
import javafx.scene.layout.Pane;

/**
 * MapWay
 */
public class MapWay implements IGUIMapElement {

    List<MapNode> nodes;

    public MapWay() {
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

}
