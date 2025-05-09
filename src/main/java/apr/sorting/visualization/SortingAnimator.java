package apr.sorting.visualization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import apr.sorting.visualization.SortingReplay.ElementAction;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * SortingAnimator
 */
public class SortingAnimator extends AnimationTimer {

    Logger logger = LoggerFactory.getLogger(SortingAnimator.class);

    Pane renderTarget;
    SortingReplay replay;

    Text titleTxt;
    Rectangle[] rects;
    double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;

    long duration_ns, lastUpdate_ns, updateFreq_ns;
    boolean done;

    public SortingAnimator(Pane renderTarget, SortingReplay replay, int duration_sec) {
        this.replay = replay;
        this.renderTarget = renderTarget;
        this.duration_ns = duration_sec * 1000000000L;
        this.updateFreq_ns = duration_ns / replay.actions.size();
        for (double val : replay.data) {
            if (val < min) {
                min = val;
            }
            if (val > max) {
                max = val;
            }
        }

        this.rects = new Rectangle[replay.data.length];
        for (int i = 0; i < rects.length; i++) {
            rects[i] = new Rectangle(20, 20, Color.BLACK);
        }
        renderTarget.getChildren().addAll(rects);
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
            return;
        }
    }

    public void draw() {
        double w = renderTarget.getWidth();
        double h = renderTarget.getHeight();
        double paddingHori = h / 25D;
        double paddingVert = w / 25D;
        double paddingRects = (w - 2 * paddingVert) / (rects.length - 1) / 2D;
        double rectW = (w - 2 * paddingVert - paddingRects * (rects.length - 1)) / rects.length;
        double rectHMin = (h - 2 * paddingHori) / 20D;
        double rectH = h - 2 * paddingHori - rectHMin;

        for (int i = 0; i < rects.length; i++) {
            double height = ((replay.data[i] - min) / (max - min)) * rectH + rectHMin;
            rects[i].widthProperty().set(rectW);
            rects[i].heightProperty().set(height);
            rects[i].relocate(rectW * i + paddingVert + paddingRects * i, h - paddingHori - height);
        }
    }

}
