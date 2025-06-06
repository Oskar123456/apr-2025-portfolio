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
import apr.examproj.config.ApplicationConfig;
import apr.examproj.enums.TransportationMode;

import static apr.examproj.enums.TransportationMode.*;
import apr.examproj.gui.*;
import apr.examproj.map.*;
import apr.examproj.osm.MapData;
import apr.examproj.utils.Geometry;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * StreetMapDriver
 */
public class StreetMapApp {

    static GUIMap map;
    static StreetMap streetMap;
    static MapAddress src, dest;
    public static MapBounds bounds;

    static TransportationMode transportationMode = WALK;
    static PathFinder<MapNode> pathFinder;
    static List<String> sortedAlgNames;
    static Map<String, PathFinder<MapNode>> pathFinders = Map.of(
            "A*", new AStar<>((a, b) -> Geometry.travelTime(a.data, b.data, WALK)),
            "dijkstra", new Dijkstra<>());
    static MapRoute route;

    /* GUI */
    static Pane renderPane;
    static HBox srcPane, destPane;
    static ToolPanel toolPanel;
    static TextPanel textPanel;
    static HBox ruler;
    static Text rulerText;
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
        map.scaleXProperty().addListener(e -> srcPane.scaleXProperty().set(1 / map.scaleXProperty().get()));
        map.scaleYProperty().addListener(e -> srcPane.scaleYProperty().set(1 / map.scaleYProperty().get()));
        map.scaleXProperty().addListener(e -> destPane.scaleXProperty().set(1 / map.scaleXProperty().get()));
        map.scaleYProperty().addListener(e -> destPane.scaleYProperty().set(1 / map.scaleYProperty().get()));

        sortedAlgNames = new ArrayList<>(pathFinders.keySet().stream().toList());
        sortedAlgNames.sort(Comparator.naturalOrder());
        pathFinder = pathFinders.get(sortedAlgNames.getFirst());

        setToolPanel();
        setTextPanel();

        renderPane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> Options.hide());
        renderPane.addEventHandler(ScrollEvent.SCROLL, e -> map.zoom(zoomSpeed * e.getDeltaY()));
        renderPane.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.getKeyCode("I")) {
                map.zoom(0.05);
            }
            if (e.getCode() == KeyCode.getKeyCode("O")) {
                map.zoom(-0.05);
            }
            if (e.getCode() == KeyCode.getKeyCode("R")) {
                map.translateXProperty().set(renderPane.getWidth() / 2);
                map.translateYProperty().set(renderPane.getHeight() / 2);
            }
        });
        renderPane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            mouseX = e.getX();
            mouseY = e.getY();
        });
        draggable(renderPane, map);

        renderPane.getChildren().addAll(map, toolPanel, textPanel);
        map.getChildren().addAll(srcPane, destPane);
        Options.getInstance().setRenderTarget(map);
        Tooltip.getInstance().setRenderTarget(map);
        Ruler.getInstance().setRenderTarget(renderPane);

        toolPanel.reposition(renderPane);
        textPanel.reposition(renderPane);

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
        }
    }

    static void pause() {
        if (pathingAnimator == null) {
            return;
        }
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

    static void draggable(Pane outer, Pane inner) {
        outer.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            if (Double.isNaN(mouseX)) {
                return;
            }
            if (Double.isNaN(mouseY)) {
                return;
            }
            if (mouseX < 0 || mouseX >= renderPane.getWidth()) {
                return;
            }
            if (mouseY < 0 || mouseY >= renderPane.getHeight()) {
                return;
            }
            double dx = e.getX() - mouseX;
            double dy = e.getY() - mouseY;
            inner.translateXProperty().set(inner.getTranslateX() + dragSpeed * dx);
            inner.translateYProperty().set(inner.getTranslateY() + dragSpeed * dy);
            mouseX = e.getX();
            mouseY = e.getY();
        });
    }

    static void setToolPanel() {

        toolPanel = new ToolPanel();
        toolPanel.addButton(e -> run(), "run");
        toolPanel.addButton(e -> pause(), "pause");
        toolPanel.addButton(e -> {
            ApplicationConfig.showLinkPaths.setValue(!ApplicationConfig.showLinkPaths.getValue());
        }, "show link paths");
        toolPanel.addButton(e -> {
            ApplicationConfig.showPathNodes.setValue(!ApplicationConfig.showPathNodes.getValue());
        }, "show path nodes");
        toolPanel.addSlider("Duration: %d secs", 1, 20, 5, (e, o, n) -> {
            if (pathingAnimator != null) {
                PathingAnimator.setDuration(n.longValue());
            }
        });
        toolPanel.addChoiceBox((e, o, n) -> pathFinder = pathFinders.get(n), sortedAlgNames);
        toolPanel.position(renderPane);

    }

    static void setTextPanel() {
        textPanel = new TextPanel();
        textPanel.setTexts("Route");
        textPanel.position(renderPane);
    }

}
