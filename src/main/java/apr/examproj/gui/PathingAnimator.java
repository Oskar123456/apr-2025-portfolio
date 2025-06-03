package apr.examproj.gui;

import java.util.ArrayList;
import java.util.List;

import apr.examproj.ds.Graph;
import apr.examproj.map.MapBounds;
import apr.examproj.map.MapNode;
import apr.examproj.map.MapRoute;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

/**
 * PathingAnimator
 */
public class PathingAnimator extends AnimationTimer {

    static final long bln = 1000000000L;
    static long durationS = 5;

    Pane renderPane;
    Parent containerPane;
    TextPanel textPanel;

    long startNS, lastUpdateNS, deltaNS;
    long updateFreqNS;

    int lastFrame = 0, frame, frameInc = 1;
    Graph<MapNode> graph;
    MapRoute route;
    MapBounds bounds;

    List<Node> guiElements = new ArrayList<>();

    public PathingAnimator(Pane renderPane, MapBounds bounds, MapRoute route) {
        this.renderPane = renderPane;
        this.graph = route.graph;
        this.route = route;
        this.bounds = bounds;
        setDuration(durationS);
    }

    public static void setDuration(long seconds) {
        durationS = seconds;
    }

    @Override
    public void handle(long nowNS) {
        updateFreqNS = (durationS * bln) / route.graph.getVisitOrder().size();
        frameInc = Math.max((int) ((bln / 60) / updateFreqNS), 1);
        if (startNS == 0) {
            startNS = nowNS;
            lastUpdateNS = nowNS;
        }
        deltaNS = nowNS - lastUpdateNS;
        if (deltaNS < updateFreqNS) {
            return;
        }
        lastUpdateNS = nowNS;

        draw();

        if (textPanel != null) {
            textPanel.setTexts(describe());
        }

        frame = Math.min(frame + frameInc, route.graph.getVisitOrder().size());
    }

    public void detach() {
        for (var elmt : guiElements) {
            renderPane.getChildren().remove(elmt);
        }
    }

    public void draw() {
        if (frame < graph.getVisitOrder().size()) {
            drawPathing();
        } else {
            drawRoute();
        }
    }

    void drawPathing() {
        for (int i = 0; i < lastFrame; ++i) {
            ((Pane) guiElements.get(i)).getChildren().getFirst().setId("street-map__visited-node");
        }
        for (int i = lastFrame; i < frame; ++i) {
            var node = graph.getVisitOrder().get(i);
            var mapNode = node.data;
            var mapGUINode = GUIFactory.highlightedMapNode(mapNode);
            guiElements.add(mapGUINode);
            Tooltip.setTooltip(mapGUINode,
                    String.format("lat: %.4f, lon: %.4f", mapNode.lat, mapNode.lon),
                    String.format("dist: %s", GUIUtils.timeFormat(graph.dist(node))),
                    "id: " + mapNode.id);
            renderPane.getChildren().add(mapGUINode);
        }
        lastFrame = frame;
    }

    String[] describe() {
        if (frame < graph.getVisitOrder().size()) {
            return new String[] {
                    route.getDescription(),
                    "Nodes visited: " + (frame + 1)
            };
        } else {
            return new String[] {
                    route.getDescription(),
                    "Time: " + GUIUtils.timeFormat(route.hours),
                    String.format("Length: %.1fm", route.dist),
                    "Nodes: " + graph.getVisitOrder().size()
            };
        }
    }

    void drawRoute() {
        for (var elmt : guiElements) {
            renderPane.getChildren().remove(elmt);
        }
        guiElements.clear();
        var nodes = route.drawNodes();
        guiElements.addAll(nodes);
        renderPane.getChildren().addAll(guiElements);
        System.out.println(
                "PathingAnimator.drawRoute(): took " + (((double) lastUpdateNS - startNS) / bln) + " secs to complete");
        stop();
    }

    public void setTextPanel(TextPanel textPanel) {
        this.textPanel = textPanel;
    }

}
