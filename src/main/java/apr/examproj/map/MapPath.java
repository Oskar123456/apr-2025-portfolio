package apr.examproj.map;

import java.util.ArrayList;
import java.util.List;

import apr.examproj.gui.GUIUtils;
import apr.examproj.gui.IGUIMapElement;
import javafx.scene.Node;
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
    }

    @Override
    public Node tooltip(Pane parentPane) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tooltip'");
    }

    @Override
    public void setHoverTooltipTarget(Pane tooltipTarget) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setHoverTooltipTarget'");
    }

}
