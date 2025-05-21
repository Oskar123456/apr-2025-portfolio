package apr.examproj.gui;

import apr.examproj.geom.MapBounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * Guifiable
 */
public interface GUIMapElement {

    public Node guify(Pane parentPane, MapBounds mapBounds);

}
