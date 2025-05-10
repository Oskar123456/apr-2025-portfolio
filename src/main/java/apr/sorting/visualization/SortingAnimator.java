package apr.sorting.visualization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import apr.sorting.visualization.SortingReplay.ActionType;
import apr.sorting.visualization.SortingReplay.ElementAction;
import apr.utilities.Tools;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * SortingAnimator
 */
public class SortingAnimator extends AnimationTimer {

    Logger logger = LoggerFactory.getLogger(SortingAnimator.class);

    Pane renderTarget;
    Pane canvas, infoBox, timeBox;
    SortingReplay<Double> replay;

    Text titleTxt, infoTxt, statsTxt;
    Rectangle[] rects;
    double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;

    Color defaultColor = Color.BLACK;
    Color compareColor = Color.YELLOW;
    Color swapColor = Color.RED;

    String statsTemplate = "Stats: swaps: %d, comparisons: %d";
    int statsSwaps, statsComparisons;

    long start_ns, lastUpdate_ns, updateFreq_ns;
    boolean done;

    public SortingAnimator(String title, Pane renderTarget, SortingReplay<Double> replay, int ups) {
        canvas = new Pane();
        canvas.setId("sorting__canvas");

        timeBox = new HBox(4);
        timeBox.setId("sorting__action-box");
        infoTxt = new Text();
        infoTxt.setId("sorting__info-text");
        titleTxt = new Text(title);
        titleTxt.setId("sorting__title-text");
        timeBox.getChildren().addAll(new Text("Time elapsed:"), infoTxt);

        statsTxt = new Text();
        updateStats();
        infoBox = new VBox();
        infoBox.setId("sorting__info-box");
        infoBox.getChildren().addAll(titleTxt, timeBox, statsTxt);

        infoBox.prefHeightProperty().set(50);
        infoBox.prefWidthProperty().bind(renderTarget.widthProperty().subtract(8));

        canvas.prefWidthProperty().bind(renderTarget.widthProperty().subtract(8));
        canvas.prefHeightProperty().bind(renderTarget.heightProperty().subtract(infoBox.heightProperty()));

        renderTarget.getChildren().add(infoBox);
        renderTarget.getChildren().add(canvas);

        this.replay = replay;
        this.renderTarget = renderTarget;
        setUps(ups);

        canvas.widthProperty().addListener(e -> draw());
        canvas.heightProperty().addListener(e -> draw());

        System.out.printf("SortingAnimator (%s):: dur: %f secs, updateFreq: %f (%d elements, %d actions)%n",
                title, (replay.actions.size() / (double) ups),
                Tools.NanoSecsToSecs(updateFreq_ns), replay.len, replay.actions.size());

        for (Double val : replay.dataInit) {
            if (val < min) {
                min = val;
            }
            if (val > max) {
                max = val;
            }
        }

        this.rects = new Rectangle[replay.dataInit.length];
        for (int i = 0; i < rects.length; i++) {
            rects[i] = new Rectangle(20, 20, Color.BLACK);
        }

        canvas.getChildren().addAll(rects);
        draw();
    }

    public void handle(long now_ns) {
        if (done) {
            return;
        }
        if (!(lastUpdate_ns == 0) && now_ns - lastUpdate_ns < updateFreq_ns) {
            return;
        }
        if (lastUpdate_ns == 0) {
            start_ns = now_ns;
        }

        lastUpdate_ns = now_ns;

        ElementAction nextAction = replay.next();
        if (nextAction == null) {
            done = true;
            draw();
            stop();
            return;
        }

        if (nextAction.action == ActionType.COMPARE) {
            statsComparisons++;
        }
        if (nextAction.action == ActionType.SWAP) {
            statsSwaps++;
        }

        updateStats();
        draw();
    }

    public void setUps(int ups) {
        this.updateFreq_ns = 1000000000L / ups;
    }

    public void draw() {
        /* RESIZE */
        double w = canvas.getWidth();
        double h = canvas.getHeight() - infoBox.heightProperty().doubleValue();
        /* TEXT */
        infoTxt.setText(String.format("%.1f seconds", Tools.NanoSecsToSecs(lastUpdate_ns - start_ns)));

        /* VISUALIZATION */
        double padding = 4;
        double paddingRects = (w - 2 * padding) / (rects.length - 1) / 2D;
        double rectW = (w - paddingRects * (rects.length - 1) - 2 * padding) / rects.length;
        double rectHMin = h / 20D;
        double rectH = h - rectHMin;

        for (int i = 0; i < rects.length; i++) {
            double height = ((replay.dataInit[i] - min) / (max - min)) * rectH + rectHMin;
            rects[i].widthProperty().set(rectW);
            rects[i].heightProperty().set(height);
            rects[i].relocate(rectW * i + paddingRects * i + padding, canvas.getHeight() - height);
            rects[i].fillProperty().set(defaultColor);
        }

        if (replay.isDone) {
            return;
        }

        ElementAction lastAction = replay.last();
        if (lastAction == null) {
            return;
        }

        if (lastAction.action == ActionType.COMPARE) {
            rects[lastAction.first].fillProperty().set(compareColor);
            rects[lastAction.second].fillProperty().set(compareColor);
        }
        if (lastAction.action == ActionType.SWAP) {
            rects[lastAction.first].fillProperty().set(swapColor);
            rects[lastAction.second].fillProperty().set(swapColor);
        }
    }

    void updateStats() {
        statsTxt.setText(String.format(statsTemplate, statsSwaps, statsComparisons));
    }

}
