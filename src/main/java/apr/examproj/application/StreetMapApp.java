package apr.examproj.application;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import apr.examproj.alg.AStar;
import apr.examproj.alg.Dijkstra;
import apr.examproj.alg.PathFinder;
import apr.examproj.enums.TransportationMode;

import static apr.examproj.enums.TransportationMode.*;
import apr.examproj.gui.GUIFactory;
import apr.examproj.gui.GUIMap;
import apr.examproj.gui.Options;
import apr.examproj.gui.PathingAnimator;
import apr.examproj.gui.TextPanel;
import apr.examproj.gui.ToolPanel;
import apr.examproj.gui.Tooltip;
import apr.examproj.map.MapAddress;
import apr.examproj.map.MapBounds;
import apr.examproj.map.MapFactory;
import apr.examproj.map.MapNode;
import apr.examproj.map.MapRoute;
import apr.examproj.map.StreetMap;
import apr.examproj.osm.MapData;
import apr.examproj.utils.Geometry;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * StreetMapDriver
 */
public class StreetMapApp {

    static GUIMap map;
    static StreetMap streetMap;
    static MapAddress src, dest;
    static MapBounds bounds;

    static TransportationMode transportationMode = WALK;
    static PathFinder<MapNode> pathFinder;
    static Map<String, PathFinder<MapNode>> pathFinders = Map.of(
            "A*", new AStar<>((a, b) -> Geometry.travelTime(a.data, b.data, WALK)),
            "dijkstra", new Dijkstra<>());
    static MapRoute route;

    /* GUI */
    static Pane renderPane;
    static HBox srcPane, destPane;
    static ToolPanel toolPanel;
    static TextPanel textPanel;
    static double mouseX, mouseY;
    static double dragSpeed = 1;
    static double zoomSpeed = 0.0008;
    static PathingAnimator pathingAnimator;
    static boolean paused;

    public static void start(String osmPath, Pane renderPane) throws IOException {
        init(osmPath);

        System.out.println("StreetMapApp2.start(): streetMap = " + streetMap);

        initGUI(renderPane);

        System.out.println("StreetMapApp2.start(): bounds: " + bounds
                + " (" + bounds.width + "," + bounds.height + " / "
                + renderPane.getWidth() + "," + renderPane.getHeight() + ")");
    }

    static void init(String osmPath) throws IOException {
        String osmStr = new String(Files.readAllBytes(Paths.get(osmPath)));
        MapData mapData = new MapData(osmStr);
        bounds = MapFactory.bounds(mapData.getBounds());
        streetMap = new StreetMap(mapData);
        map = new GUIMap(streetMap);
    }

    static void initGUI(Pane renderPane) {
        StreetMapApp.renderPane = renderPane;

        srcPane = new HBox();
        srcPane.setId("street-map__src-pane");
        destPane = new HBox();
        destPane.setId("street-map__dest-pane");
        GUIFactory.defaultChildText(srcPane, "start", "street-map__src-text");
        GUIFactory.defaultChildText(destPane, "end", "street-map__dest-text");

        toolPanel = new ToolPanel();
        toolPanel.addButton(e -> run(), "run");
        toolPanel.addButton(e -> pause(), "pause");
        toolPanel.addSlider("Duration: %d secs", 1, 20, 5, (e, o, n) -> {
            if (pathingAnimator != null) {
                PathingAnimator.setDuration(n.longValue());
            }
        });
        toolPanel.position(renderPane);

        textPanel = new TextPanel();
        textPanel.setTexts("Route");
        textPanel.position(renderPane);

        List<String> sortedAlgNames = new ArrayList<>(
                pathFinders.keySet().stream().toList());
        sortedAlgNames.sort(Comparator.naturalOrder());
        toolPanel.addChoiceBox((e, o, n) -> pathFinder = pathFinders.get(n), sortedAlgNames);
        pathFinder = pathFinders.get(sortedAlgNames.getFirst());

        renderPane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> Options.hide());
        renderPane.addEventHandler(ScrollEvent.SCROLL, e -> map.zoom(zoomSpeed * e.getDeltaY()));

        renderPane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });
        renderPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            double dx = e.getX() - mouseX;
            double dy = e.getY() - mouseY;
            map.translateXProperty().set(map.getTranslateX() + dragSpeed * dx);
            map.translateYProperty().set(map.getTranslateY() + dragSpeed * dy);
            mouseX = e.getX();
            mouseY = e.getY();
        });

        renderPane.getChildren().addAll(map, toolPanel, textPanel);
        map.getChildren().addAll(srcPane, destPane);
        Options.getInstance().setRenderTarget(map);
        Tooltip.getInstance().setRenderTarget(map);

        toolPanel.reposition(renderPane);
        textPanel.reposition(renderPane);

        Options.hide();
        Tooltip.hide();
        srcPane.setVisible(false);
        destPane.setVisible(false);
    }

    public static void setSrc(MapAddress node) {
        if (dest == node) {
            dest = null;
        }
        src = node;
        route = null;
        Geometry.relocateToScreenCoords(srcPane, src.node.getPos());
        srcPane.setVisible(true);
    }

    public static void setDest(MapAddress node) {
        if (src == node) {
            src = null;
        }
        dest = node;
        route = null;
        Geometry.relocateToScreenCoords(destPane, dest.node.getPos());
        destPane.setVisible(true);
    }

    static void run() {
        if (src == null || dest == null) {
            System.out.println("StreetMapApp.run(): error: missing source or destination");
            return;
        }
        try {
            System.out.println("StreetMapApp.run(): " + pathFinder.getClass().getSimpleName());
            route = streetMap.getRoute(transportationMode, pathFinder, src, dest);
            if (pathingAnimator != null) {
                pathingAnimator.detach();
                pathingAnimator.stop();
            }
            pathingAnimator = new PathingAnimator(map, bounds, route);
            pathingAnimator.setTextPanel(textPanel);
            pathingAnimator.start();
            paused = false;
        } catch (Exception e) {
            System.out.println("StreetMapApp.run(): error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void pause() {
        if (isPaused()) {
            paused = false;
            pathingAnimator.start();
        } else {
            paused = true;
            pathingAnimator.stop();
        }
    }

    static boolean isPaused() {
        return paused;
    }

}
