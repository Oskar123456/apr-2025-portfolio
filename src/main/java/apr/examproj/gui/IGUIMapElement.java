package apr.examproj.gui;

import apr.examproj.map.MapBounds;
import javafx.scene.layout.Pane;

/**
 * IGUIElement
 */
public interface IGUIMapElement {

    public void draw(MapBounds bounds, Pane renderPane);

}
