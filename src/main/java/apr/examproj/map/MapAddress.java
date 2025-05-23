package apr.examproj.map;

import apr.examproj.gui.IGUIMapElement;
import javafx.scene.layout.Pane;

/**
 * MapAddress
 */
public class MapAddress implements IGUIMapElement {

    public String id;
    public String city;
    public String country;
    public String municipality;
    public String postcode;
    public String street;
    public String housenumber;
    public String nodeId;

    public MapAddress() {
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'draw'");
    }

}
