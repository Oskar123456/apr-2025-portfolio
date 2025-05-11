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
import javafx.scene.text.Text;

/**
 * GraphAnimator
 */
public class GraphAnimator extends AnimationTimer {

    /* GUI */
    Pane renderTarget, canvas;
    VBox layout, content;
    HBox topPanel;
    Text title;
    String name;

    Color colorDefault = Color.WHITESMOKE;
    Color colorSeen = Color.GRAY;
    Color colorVis = Color.BLACK;
    Color colorEdge = Color.BLACK;

    /* STATE */
    GraphReplay<Integer> replay;
    Graph<Integer> graph;
    int ups = 5, nodeId;
    long lastUpdateNS, updateFreqNS;
    double nodeRadius = 0.025;

    Node<Integer> edgeSrc, edgeDest;
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

    public void search(String title, GraphAlg<Integer> graphAlg) {
        if (graph.src == null || graph.dest == null) {
            System.out.println("GraphAnimator:: Error: no src or dest, returning...");
            return;
        }
        this.title.setText(title);
        replay = new GraphReplay<>(graph);
        graphAlg.search(graph);
    }

    public void handle(long nowNS) {
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
        double wPad = 0D; // (canvas.widthProperty().doubleValue() - len) / 2D;
        double hPad = 0D; // (canvas.heightProperty().doubleValue() - len) / 2D;
        double r = nodeRadius * len;

        // System.out.printf("draw():: len: %f, wPad: %f, hPad: %f%n", len, wPad, hPad);

        for (var edge : graph.edges) {
            double xSrc = wPad + edge.src.pos.x * w + r / 2;
            double ySrc = hPad + edge.src.pos.y * h + r / 2;
            double xDest = wPad + edge.dest.pos.x * w + r / 2;
            double yDest = hPad + edge.dest.pos.y * h + r / 2;

            Line l = new Line(xSrc, ySrc, xDest, yDest);
            l.fillProperty().set(colorEdge);
            l.strokeWidthProperty().set(2);

            Ellipse e = new Ellipse(r, r);
            e.fillProperty().set(Color.WHITE);
            e.relocate((Math.max(xSrc, xDest) - Math.min(xSrc, xDest)) / 2 + Math.min(xSrc, xDest) - r,
                    (Math.max(ySrc, yDest) - Math.min(ySrc, yDest)) / 2 + Math.min(ySrc, yDest) - r);

            Text weightTxt = new Text(String.format("%.1f", edge.weight));
            weightTxt.maxWidth(2 * r);
            weightTxt.relocate((Math.max(xSrc, xDest) - Math.min(xSrc, xDest)) / 2 + Math.min(xSrc, xDest) - r / 2,
                    (Math.max(ySrc, yDest) - Math.min(ySrc, yDest)) / 2 + Math.min(ySrc, yDest) - r / 2);
            weightTxt.fillProperty().set(Color.RED);

            canvas.getChildren().add(l);
            canvas.getChildren().add(e);
            canvas.getChildren().add(weightTxt);
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
                    description = String.format("%.1f", graph.dists.get(node));
                } else {
                    description = "INF";
                }
            }

            Text txt = new Text(description);

            // System.out.printf("draw():: add ellipse: w/h: %f, at (%f, %f%n",
            // r, x, y);

            canvas.getChildren().add(e);
            canvas.getChildren().add(txt);

            e.relocate(x, y);
            txt.relocate(x + 0.5 * r, y + 0.5 * r);

            e.addEventHandler(MouseEvent.MOUSE_PRESSED, (event) -> addEdge(event, node));
        }
    }

    void setupGUI() {
        layout = new VBox();
        layout.setId("graph__layout");
        canvas = new Pane();
        canvas.setId("graph__canvas");
        topPanel = new HBox();
        topPanel.setId("graph__topPanel");
        title = new Text(name);
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
        topPanel.getChildren().add(title);

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

            // System.out.printf("added node at %s on canvas(%.1f %.1f)%n",
            // node.pos.toString(), canvas.widthProperty().doubleValue(),
            // canvas.heightProperty().doubleValue());

            System.out.println(graph.toString());
            graph.nodes.add(node);
            draw();
        });

    }

    void addEdge(MouseEvent event, Node<Integer> node) {
        event.consume();

        if (event.isSecondaryButtonDown()) {
            if (srcDestPick == 0) {

            } else {

            }
            srcDestPick = (srcDestPick + 1) % 2;
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
            System.out.println(graph.toString());
            draw();
        }
    }

}
