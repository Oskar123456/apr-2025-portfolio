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
    Pane canvas, infoBox, actionBox;
    SortingReplay<Double> replay;

    Text infoTxt, statsTxt;
    Rectangle[] rects;
    double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;

    Color defaultColor = Color.BLACK;
    Color compareColor = Color.YELLOW;
    Color swapColor = Color.RED;

    String statsTemplate = "Stats: swaps: %d, comparisons: %d";
    int statsSwaps, statsComparisons;

    long duration_ns, lastUpdate_ns, updateFreq_ns;
    boolean done;

    public SortingAnimator(Pane renderTarget, SortingReplay<Double> replay, int duration_sec) {
        canvas = new Pane();
        canvas.setId("sorting__canvas");

        actionBox = new HBox(30);
        actionBox.setId("sorting__action-box");
        infoTxt = new Text();
        infoTxt.setId("sorting__info-text");
        actionBox.getChildren().addAll(new Text("Last action:"), infoTxt);

        statsTxt = new Text();
        updateStats();
        infoBox = new VBox();
        infoBox.setId("sorting__info-box");
        infoBox.getChildren().addAll(actionBox, statsTxt);

        infoBox.prefHeightProperty().set(50);
        infoBox.prefWidthProperty().bind(renderTarget.widthProperty().subtract(8));
        canvas.prefWidthProperty().bind(renderTarget.widthProperty().subtract(8));
        canvas.prefHeightProperty().bind(renderTarget.heightProperty().subtract(50));

        renderTarget.getChildren().add(infoBox);
        renderTarget.getChildren().add(canvas);

        this.replay = replay;
        this.renderTarget = renderTarget;
        this.duration_ns = duration_sec * 1000000000L;
        this.updateFreq_ns = duration_ns / replay.actions.size();

        System.out.printf("SortingAnimator:: dur: %f, updateFreq: %f (%d elements, %d actions)%n",
                Tools.NanoSecsToSecs(duration_ns),
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

        lastUpdate_ns = now_ns;

        ElementAction nextAction = replay.next();
        if (nextAction == null) {
            done = true;
            draw();
            System.out.println("DONE");
            stop();
            return;
        }

        updateStats();
        draw();
    }

    public void draw() {
        /* RESIZE */
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        /* TEXT */
        infoTxt.setText("");

        /* VISUALIZATION */
        double paddingHori = h / 25D;
        double paddingVert = w / 25D;
        double paddingRects = (w - 2 * paddingVert) / (rects.length - 1) / 2D;
        double rectW = (w - 2 * paddingVert - paddingRects * (rects.length - 1)) / rects.length;
        double rectHMin = (h - 2 * paddingHori) / 20D;
        double rectH = h - 2 * paddingHori - rectHMin;

        for (int i = 0; i < rects.length; i++) {
            double height = ((replay.dataInit[i] - min) / (max - min)) * rectH + rectHMin;
            rects[i].widthProperty().set(rectW);
            rects[i].heightProperty().set(height);
            rects[i].relocate(rectW * i + paddingVert + paddingRects * i, h - paddingHori - height);
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
            statsComparisons++;
            infoTxt.setText("Compare");
        }
        if (lastAction.action == ActionType.SWAP) {
            rects[lastAction.first].fillProperty().set(swapColor);
            rects[lastAction.second].fillProperty().set(swapColor);
            statsSwaps++;
            infoTxt.setText("Swap");
        }
    }

    void updateStats() {
        statsTxt.setText(String.format(statsTemplate, statsSwaps, statsComparisons));
    }

}
