package apr.examproj.gui;

import apr.examproj.map.MapBounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * IGUIElement
 */
public interface IGUIMapElement {

    public void draw(MapBounds bounds, Pane renderPane);

    public Node tooltip(Pane parentPane);

    public void setHoverTooltipTarget(Pane tooltipTarget);

}
