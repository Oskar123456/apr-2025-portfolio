package apr.examproj.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import apr.examproj.alg.Dijkstra;
import apr.examproj.alg.PathFinder;
import apr.examproj.ds.Graph;
import apr.examproj.enums.TransportationMode;
import apr.examproj.gui.GUIFactory;
import apr.examproj.gui.GUIUtils;
import apr.examproj.gui.Options;
import apr.examproj.gui.ToolPanel;
import apr.examproj.gui.Tooltip;
import apr.examproj.map.MapAddress;
import apr.examproj.map.MapBounds;
import apr.examproj.map.MapFactory;
import apr.examproj.map.MapNode;
import apr.examproj.map.MapRoute;
import apr.examproj.map.StreetMap;
import apr.examproj.osm.MapData;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * StreetMapDriver
 */
public class StreetMapApp {

    static StreetMap streetMap;
    static MapAddress src, dest;
    static MapBounds bounds;

    static Graph<MapNode> graph;
    static TransportationMode transportationMode = TransportationMode.WALK;
    static PathFinder<MapNode> pathFinder;
    static MapRoute route;

    Map<String, PathFinder<MapNode>> pathFinders = Map.of("dijkstra", (g) -> );

    /* GUI */
    static Pane renderPane;
    static HBox srcPane, destPane;
    static ToolPanel toolPanel;

    public static void start(Pane renderPane) throws IOException {
        String osmStr = new String(Files.readAllBytes(Paths.get("data/map.osm")));
        MapData mapData = new MapData(osmStr);

        bounds = MapFactory.bounds(mapData.getBounds());
        streetMap = new StreetMap(mapData);
        streetMap.setRenderTarget(renderPane);

        /* DEBUG PRINTING */
        System.out.println("StreetMapDriver.start()");
        System.out.println(streetMap);

        initGUI(renderPane);
        draw();
    }

    static void draw() {
        renderPane.getChildren().clear();
        streetMap.draw(bounds, renderPane);

        if (src != null) {
            var srcPos = GUIUtils.mapNodeCoordsToPane(bounds, renderPane, src.node);
            srcPane.relocate(srcPos.x - destPane.getWidth() / 2, srcPos.y + MapAddress.getRadius(renderPane) + 5);
            srcPane.setVisible(true);
        } else {
            srcPane.setVisible(false);
        }

        if (dest != null) {
            var destPos = GUIUtils.mapNodeCoordsToPane(bounds, renderPane, dest.node);
            destPane.relocate(destPos.x - destPane.getWidth() / 2, destPos.y + MapAddress.getRadius(renderPane) + 5);
            destPane.setVisible(true);
        } else {
            destPane.setVisible(false);
        }

        if (route != null) {
            route.draw(bounds, renderPane);
        }

        toolPanel.position(renderPane);
        renderPane.getChildren().addAll(srcPane, destPane, toolPanel);

        Options.getInstance().setRenderTarget(renderPane);
        Options.hide();
        Tooltip.getInstance().setRenderTarget(renderPane);
        Tooltip.hide();
    }

    static void initGUI(Pane renderPane) {
        StreetMapApp.renderPane = renderPane;
        srcPane = new HBox();
        srcPane.setId("street-map__src-pane");
        destPane = new HBox();
        destPane.setId("street-map__dest-pane");
        var srcTxt = GUIFactory.defaultChildText(srcPane, "start", "street-map__src-text");
        var destTxt = GUIFactory.defaultChildText(destPane, "end", "street-map__dest-text");

        toolPanel = new ToolPanel();
        toolPanel.addButton(e -> run(), "run");
        toolPanel.addChoiceBox((e, o, n) -> System.out.println(n), List.of("dijkstra", "A*"));

        renderPane.widthProperty().addListener((e) -> draw());
        renderPane.heightProperty().addListener((e) -> draw());

        renderPane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> Options.hide());
    }

    public static void setSrc(MapAddress node) {
        if (dest == node) {
            dest = null;
        }
        src = node;
        System.out.println("StreetMapApp.setSrc(): " + src);
        draw();
    }

    public static void setDest(MapAddress node) {
        if (src == node) {
            src = null;
        }
        dest = node;
        System.out.println("StreetMapApp.setDest(): " + dest);
        draw();
    }

    static void run() {
        if (src == null || dest == null) {
            System.out.println("StreetMapApp.run(): error: missing source or destination");
            return;
        }
        try {
            route = streetMap.getRoute(transportationMode, pathFinder, src, dest);
            System.out.println("StreetMapApp.run(): " + route);
        } catch (Exception e) {
            System.out.println("StreetMapApp.run(): error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void setAlg(String name) {
        switch (name) {
            case "dijkstra":
                pathFinder = Dijkstra;
                break;

            default:
                break;
        }
    }

}
