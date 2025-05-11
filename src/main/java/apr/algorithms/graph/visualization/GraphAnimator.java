package apr.algorithms.graph.visualization;

import apr.datastructures.graph.Point2D;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

/**
 * GraphAnimator
 */
public class GraphAnimator extends AnimationTimer {

    /* GUI */
    Pane renderTarget, canvas;
    VBox layout, content, topPanel;
    Text title, info;
    String name;

    Color colorDefault = Color.WHITESMOKE;
    Color colorSeen = Color.LIGHTGRAY;
    Color colorVis = Color.GRAY;
    Color colorEdge = Color.BLACK;

    /* STATE */
    GraphReplay<Integer> replay;
    Graph<Integer> graph;
    int ups = 5, nodeId;
    long startNS, lastUpdateNS, updateFreqNS;
    double rProp = 0.030, r;

    Node<Integer> edgeSrc, edgeDest;
    Point2D srcPos, destPos;
    Line edgeLine;
    int srcDestPick;

    public GraphAnimator(Pane renderTarget, String name) {
        this.name = name;
        this.renderTarget = renderTarget;
        this.graph = new Graph<>();

        this.updateFreqNS = 1000000000L / ups;

        setupGUI();
    }

    public GraphAnimator(Pane renderTarget, GraphReplay<Integer> replay, String name) {
        this.name = name;
        this.renderTarget = renderTarget;
        this.replay = replay;
        this.graph = new Graph<>();

        this.updateFreqNS = 1000000000L / ups;

        setupGUI();
    }

    public void setUps(int ups) {
        this.ups = ups;
    }

    public void setAlg() {

    }

    public void reset() {
        stop();
        this.graph = new Graph<>();
        this.replay = null;
        draw();
    }

    public void start() {
        startNS = System.nanoTime();
        if (replay != null) {
            // replay.reset();
            search("A*", (g) -> GraphSolver.astar(g, (a, b) -> a.pos.dist(b.pos)));
            super.start();
        } else if (graph != null) {
            search("A*", (g) -> GraphSolver.astar(g, (a, b) -> a.pos.dist(b.pos)));
            super.start();
        } else {
            System.out.println("GraphAnimator refuses to start");
        }
    }

    public void search(String title, GraphAlg<Integer> graphAlg) {
        // this.title.setText(title);

        if (graph.src == null || graph.dest == null) {
            System.out.println("GraphAnimator:: Error: no src or dest, returning...");
            return;
        }

        replay = graphAlg.search(graph);
    }

    public void handle(long nowNS) {
        if (startNS == 0) {
            startNS = System.nanoTime();
        }

        if (lastUpdateNS != 0 && nowNS - lastUpdateNS < updateFreqNS) {
            return;
        }

        lastUpdateNS = nowNS;

        if (replay == null) {
            return;
        }

        if (!replay.hasNext()) {
            return;
        }

        if (canvas.widthProperty().doubleValue() < 1 || canvas.widthProperty().doubleValue() < 1) {
            return;
        }

        graph = replay.next();
        draw();
    }

    public void draw() {
        if (graph == null) {
            return;
        }

        canvas.getChildren().clear();

        double len = Math.min(canvas.widthProperty().doubleValue(), canvas.heightProperty().doubleValue());
        double w = canvas.widthProperty().doubleValue();
        double h = canvas.heightProperty().doubleValue();
        double wPad = 0D;
        double hPad = 0D;
        r = rProp * len;

        if (replay != null && replay.hasNext()) {
            info.setText(String.format("Number of moves: %d", replay.getCurrentIndex() + 1));
        } else if (replay != null) {
            info.setText(String.format("Length of final path: %.1f",
                    graph.dists.containsKey(graph.dest) ? graph.dists.get(graph.dest) : -1));
        }

        for (var edge : graph.edges) {
            double xSrc = wPad + edge.src.pos.x * w;
            double ySrc = hPad + edge.src.pos.y * h;
            double xDest = wPad + edge.dest.pos.x * w;
            double yDest = hPad + edge.dest.pos.y * h;

            Line l = new Line(xSrc, ySrc, xDest, yDest);
            l.fillProperty().set(colorEdge);
            l.strokeWidthProperty().set(2);

            Ellipse e = new Ellipse(r * 0.95, r * 0.95);
            e.fillProperty().set(Color.WHITE);
            e.relocate((Math.max(xSrc, xDest) - Math.min(xSrc, xDest)) / 2 + Math.min(xSrc, xDest) - r,
                    (Math.max(ySrc, yDest) - Math.min(ySrc, yDest)) / 2 + Math.min(ySrc, yDest) - r);

            if (graph.path.contains(edge)) {
                e.fillProperty().set(Color.GOLD);
            }

            Point2D para = new Point2D(xDest - xSrc, yDest - ySrc).normalize().scale(r / 4);
            Point2D perp = new Point2D(-yDest + ySrc, xDest - xSrc).normalize().scale(r / 4);

            Point2D p1 = new Point2D(xDest, yDest)
                    .subtract(new Point2D(xDest - xSrc, yDest - ySrc).normalize().scale(r));
            Point2D p2 = p1.subtract(para).add(perp);
            Point2D p3 = p1.subtract(para).subtract(perp);

            Polygon pointer = new Polygon(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);

            pointer.fillProperty().set(colorEdge);

            Text weightTxt = new Text(String.format("%.2f", edge.weight));
            weightTxt.maxWidth(2 * r);
            weightTxt.relocate((Math.max(xSrc, xDest) - Math.min(xSrc, xDest)) / 2 + Math.min(xSrc, xDest) - r / 2,
                    (Math.max(ySrc, yDest) - Math.min(ySrc, yDest)) / 2 + Math.min(ySrc, yDest) - r / 2);
            weightTxt.fillProperty().set(Color.RED);

            canvas.getChildren().add(l);
            canvas.getChildren().add(e);
            canvas.getChildren().add(weightTxt);
            canvas.getChildren().add(pointer);
        }

        for (var node : graph.nodes) {
            Ellipse e = new Ellipse(r, r);
            double x = wPad + node.pos.x * w - r;
            double y = hPad + node.pos.y * h - r;

            if (graph.visited.contains(node)) {
                e.fillProperty().set(colorVis);
            } else if (graph.dists.containsKey(node) && graph.dists.get(node) != Double.POSITIVE_INFINITY) {
                e.fillProperty().set(colorSeen);
            } else {
                e.fillProperty().set(colorDefault);
            }

            e.strokeWidthProperty().set(2);
            e.strokeProperty().set(colorEdge);

            String description = "";

            if (graph.dists.containsKey(node)) {
                if (graph.dists.get(node) != Double.POSITIVE_INFINITY) {
                    description = String.format("%.2f", graph.dists.get(node));
                } else {
                    description = "INF";
                }
            }

            if (graph.src == node) {
                description = "src";
                e.fillProperty().set(Color.GREEN);
            }

            if (graph.dest == node) {
                description = "dest";
                e.fillProperty().set(Color.RED);
            }

            Text txt = new Text(description);

            canvas.getChildren().add(e);
            canvas.getChildren().add(txt);

            e.relocate(x, y);
            txt.relocate(x + 0.5 * r, y + 0.5 * r);

            e.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> addEdge(event, node));
            e.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
                if (!event.isSecondaryButtonDown()) {
                    return;
                }
                if (!event.isShiftDown()) {
                    return;
                }
                graph.remove(node);
                draw();
            });
            txt.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> {
                if (!event.isSecondaryButtonDown()) {
                    return;
                }
                if (!event.isShiftDown()) {
                    return;
                }
                graph.remove(node);
                draw();
            });
            txt.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> addEdge(event, node));
        }
    }

    void setupGUI() {
        layout = new VBox();
        layout.setId("graph__layout");
        canvas = new Pane();
        canvas.setId("graph__canvas");
        topPanel = new VBox();
        topPanel.setId("graph__topPanel");
        title = new Text("How-To: Click to add nodes and edges, Right-click to set source and destination");
        info = new Text("");
        title.setId("graph__title");

        StackPane canvasStack = new StackPane();
        Pane canvasOverlay = new Pane();

        canvasStack.getChildren().setAll(canvasOverlay, canvas);

        edgeLine = new Line();
        edgeLine.visibleProperty().setValue(false);
        edgeLine.setId("graph__add-edge-line");
        edgeLine.fillProperty().set(colorEdge);
        edgeLine.strokeWidthProperty().set(2);
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, e -> {
            if (edgeSrc == null) {
                return;
            }
            edgeLine.startXProperty().set(edgeSrc.pos.x * canvas.widthProperty().doubleValue());
            edgeLine.startYProperty().set(edgeSrc.pos.y * canvas.heightProperty().doubleValue());
            edgeLine.endXProperty().set(e.getX());
            edgeLine.endYProperty().set(e.getY());
        });
        canvasOverlay.getChildren().add(edgeLine);

        layout.getChildren().addAll(topPanel, canvasStack);
        topPanel.getChildren().addAll(title, info);

        layout.prefWidthProperty().bind(renderTarget.widthProperty());
        layout.prefHeightProperty().bind(renderTarget.heightProperty());

        topPanel.prefWidthProperty().bind(layout.widthProperty());
        topPanel.prefHeightProperty().set(50);

        canvasOverlay.prefWidthProperty().bind(renderTarget.widthProperty());
        canvasOverlay.prefHeightProperty().bind(renderTarget.heightProperty());
        canvasStack.prefWidthProperty().bind(renderTarget.widthProperty());
        canvasStack.prefHeightProperty().bind(renderTarget.heightProperty());
        canvas.prefWidthProperty().bind(renderTarget.widthProperty());
        canvas.prefHeightProperty().bind(renderTarget.heightProperty());

        renderTarget.getChildren().setAll(layout);

        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            if (!e.isPrimaryButtonDown()) {
                return;
            }

            if (edgeSrc != null) {
                edgeSrc = null;
                edgeLine.visibleProperty().setValue(false);
                return;
            }

            var node = new Node<Integer>(nodeId++,
                    new Point2D(e.getX() / canvas.widthProperty().doubleValue(),
                            e.getY() / canvas.heightProperty().doubleValue()));

            double minDist = Double.MAX_VALUE;
            for (var n : graph.nodes) {
                if (minDist > n.pos.dist(node.pos)) {
                    minDist = n.pos.dist(node.pos);
                }
            }
            if (minDist < 3 * rProp) {
                return;
            }

            // System.out.printf("added node at %s on canvas(%.1f %.1f)%n",
            // node.pos.toString(), canvas.widthProperty().doubleValue(),
            // canvas.heightProperty().doubleValue());

            // System.out.println(graph.toString());
            graph.nodes.add(node);
            draw();
        });

    }

    void addEdge(MouseEvent event, Node<Integer> node) {
        event.consume();

        if (event.isSecondaryButtonDown() && !event.isShiftDown()) {
            if (node == graph.src) {
                graph.dest = node;
                graph.src = null;
            } else if (node == graph.dest) {
                graph.src = node;
                graph.dest = null;
            } else {
                if (graph.src == null) {
                    graph.src = node;
                } else {
                    graph.dest = node;
                }
                srcDestPick = (srcDestPick + 1) % 2;
            }
            draw();
        }

        if (event.isPrimaryButtonDown()) {
            if (edgeSrc == null) {
                edgeLine.visibleProperty().setValue(true);
                edgeSrc = node;
                edgeLine.startXProperty().set(event.getX());
                edgeLine.startYProperty().set(event.getY());
                edgeLine.endXProperty().set(event.getX());
                edgeLine.endYProperty().set(event.getY());
                // System.out.printf("set src: %s%n", node.pos.toString());
                return;
            }
            // System.out.printf("set dest: %s%n", node.pos.toString());
            graph.edges.add(new Edge<>(edgeSrc, node));
            edgeLine.visibleProperty().setValue(false);
            edgeSrc = null;
            // System.out.println(graph.toString());
            draw();
        }
    }

}
