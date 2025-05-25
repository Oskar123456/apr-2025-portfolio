package apr.examproj.map;

import java.util.ArrayList;
import java.util.List;

import apr.examproj.gui.GUIUtils;
import apr.examproj.gui.IGUIMapElement;
import apr.examproj.gui.Tooltip;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;

/**
 * MapWay
 */
public class MapPath implements IGUIMapElement {

    public String id;
    public String name;
    public String type;
    public double maxSpeed;

    List<MapNode> nodes = new ArrayList<>();

    public MapPath() {
    }

    public void addNode(MapNode node) {
        nodes.add(node);
    }

    @Override
    public void draw(MapBounds bounds, Pane renderPane) {
        Polyline line = new Polyline(GUIUtils.mapNodesToCoordArray(bounds, renderPane, nodes));
        line.setId("street-map__path");
        renderPane.getChildren().add(line);

        line.addEventHandler(MouseEvent.MOUSE_ENTERED, (e) -> showTooltip(renderPane, e));
        line.addEventHandler(MouseEvent.MOUSE_EXITED, (e) -> hideTooltip(renderPane));
    }

    private void hideTooltip(Pane renderPane) {
        Tooltip.getInstance().setVisible(false);
    }

    private void showTooltip(Pane renderPane, MouseEvent event) {
        Tooltip.getInstance().setTitle(toString());
        Tooltip.getInstance().setContentText(String.format("type: %s%nmax speed: %.1f", type, maxSpeed));
        Tooltip.getInstance().setFootnote(id);
        Tooltip.getInstance().setVisible(true);
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }

}
