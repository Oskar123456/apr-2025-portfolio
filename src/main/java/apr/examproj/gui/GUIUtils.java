package apr.examproj.gui;

import java.util.ArrayList;
import java.util.List;

import apr.datastructures.graph.Point2D;
import apr.examproj.map.MapBounds;
import apr.examproj.map.MapNode;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

/**
 * GUIUtils
 */
public class GUIUtils {

    public static <T extends Event> void addEventHandlerRecursive(Node node, EventType<T> eventType,
            EventHandler<? super T> eventHandler) {
        node.addEventHandler(eventType, eventHandler);
        if (!Parent.class.isAssignableFrom(Parent.class)) {
            return;
        }
        for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
            addEventHandlerRecursive(child, eventType, eventHandler);
        }
    }

    public static Point2D mapNodeCoordsToPane(MapBounds mapBounds, Pane renderPane, MapNode node) {
        return mapBounds.normalize(node.lat, node.lon).scale(renderPane.getWidth(), renderPane.getHeight());
    }

    public static Point2D mapCoordsToPane(MapBounds mapBounds, Pane renderPane, Point2D point) {
        return mapBounds.normalize(point.x, point.y).scale(renderPane.getWidth(), renderPane.getHeight());
    }

    public static double[] mapNodesToCoordArray(MapBounds mapBounds, Pane renderPane, List<MapNode> nodes) {
        List<Double> coords = new ArrayList<>();
        nodes.forEach(n -> {
            var point = GUIUtils.mapNodeCoordsToPane(mapBounds, renderPane, n);
            coords.add(point.x);
            coords.add(point.y);
        });
        double[] arr = new double[coords.size()];
        for (int i = 0; i < coords.size(); ++i) {
            arr[i] = coords.get(i);
        }
        return arr;
    }

}
