package apr.sorting.visualization;

import apr.GUIExample;
import apr.sorting.SortFn;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * SortingGUIExample
 */
public class SortingGUIExample extends GUIExample {

    VBox content, canvas;
    HBox titleBox;
    Text titleTxt;
    SortingAnimator animator;

    public SortingGUIExample(SortFn<Double> sortFn) {
        setupGui();

        int n = 25;

        Double[] data = new Double[n];
        for (int i = 0; i < data.length; ++i) {
            data[i] = Math.random();
        }

        var replay = sortFn.sort(data);

        animator = new SortingAnimator(canvas, replay, 25);

        widthProperty().addListener(e -> animator.draw());
        heightProperty().addListener(e -> animator.draw());

        // animator.start();
    }

    public void setTitle() {
        titleTxt.setText("Bubble Sort");
    }

    void setupGui() {
        content = new VBox();
        canvas = new VBox();
        canvas.setId("sorting__container");

        content.paddingProperty().set(new Insets(10));

        titleTxt = new Text("Bubble Sort");
        titleTxt.prefHeight(50);
        titleTxt.setId("sorting__title");
        titleBox = new HBox();
        titleBox.getChildren().add(titleTxt);
        titleBox.setId("sorting__title-box");

        content.prefWidthProperty().bind(widthProperty());
        content.prefHeightProperty().bind(heightProperty());
        canvas.prefWidthProperty().bind(widthProperty()
                .subtract(content.paddingProperty().getValue().getRight())
                .subtract(content.paddingProperty().getValue().getLeft()));
        canvas.prefHeightProperty().bind(heightProperty().subtract(50));

        content.getChildren().addAll(titleBox, canvas);

        getChildren().addAll(content);
    }

    public void start() {
        animator.start();
    }

    public void pause() {
        animator.stop();
    }

    public void stop() {
        animator.stop();
    }

}
