package apr.sorting.visualization;

import java.util.ArrayList;
import java.util.List;

import apr.GUIExample;
import apr.sorting.visualization.SortingReplay.ActionType;
import apr.sorting.visualization.SortingReplay.ElementAction;
import javafx.scene.layout.Pane;

/**
 * SortingGUIExample
 */
public class SortingGUIExample extends GUIExample {

    Pane canvas;
    SortingAnimator animator;

    public SortingGUIExample() {
        canvas = new Pane();
        canvas.setId("sorting__canvas");

        canvas.prefWidthProperty().bind(widthProperty());
        canvas.prefHeightProperty().bind(heightProperty());

        getChildren().add(canvas);

        List<ElementAction> actions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            actions.add(new ElementAction(1, 1, ActionType.COMPARE));
        }

        double[] data = new double[100];
        for (int i = 0; i < data.length; ++i) {
            data[i] = Math.random();
        }
        SortingReplay replay = new SortingReplay(actions, data);

        animator = new SortingAnimator(canvas, replay, 25);

        widthProperty().addListener(e -> animator.draw());
        heightProperty().addListener(e -> animator.draw());

        animator.start();
    }

    public void start() {

    }

    public void pause() {

    }

    public void stop() {

    }

}
