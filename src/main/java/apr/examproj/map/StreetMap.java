package apr.examproj.map;

import java.util.List;

import apr.examproj.gui.IGUIMapElement;
import apr.examproj.osm.MapData;
import javafx.scene.layout.Pane;

/**
 * StreetMap
 */
public class StreetMap implements IGUIMapElement {

    List<MapNode> nodes;
    List<MapWay> ways;
    List<MapEdge> edges;
    List<MapBuilding> buildings;

    public StreetMap(MapData mapData) {

    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

}
