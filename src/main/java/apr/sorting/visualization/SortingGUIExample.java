package apr.sorting.visualization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import apr.GUIExample;
import apr.sorting.SortFn;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * SortingGUIExample
 */
public class SortingGUIExample extends GUIExample {

    /* UI */
    VBox content;
    List<Pane> canvases;
    HBox titleBox;
    Text titleTxt;
    List<SortingAnimator> animators;
    List<Node> options;
    String[] subtitles;
    SortFn<Double>[] sortFns;

    /* STATE */
    boolean paused;
    int n = 25, ups = 10;

    public SortingGUIExample(String title, SortFn<Double>[] sortFns, String[] subtitles) {
        animators = new ArrayList<>();
        canvases = new ArrayList<>();
        options = new ArrayList<>();
        this.subtitles = subtitles;
        this.sortFns = sortFns;

        for (int i = 0; i < sortFns.length; i++) {
            canvases.add(new Pane());
        }

        setupGui();
        setTitle(title);
        addOptions();
        reset();
        stop();
    }

    public void setTitle(String title) {
        titleTxt.setText(title);
    }

    void setupGui() {
        content = new VBox();
        HBox canvasContainer = new HBox();

        content.paddingProperty().set(new Insets(10));

        titleTxt = new Text();
        titleTxt.setId("sorting__title");
        titleBox = new HBox();
        titleBox.getChildren().add(titleTxt);
        titleBox.setId("sorting__title-box");
        titleBox.prefHeight(50);

        content.prefWidthProperty().bind(widthProperty());
        content.prefHeightProperty().bind(heightProperty());

        canvasContainer.prefWidthProperty().bind(content.widthProperty()
                .subtract(content.paddingProperty().getValue().getRight())
                .subtract(content.paddingProperty().getValue().getLeft()));
        canvasContainer.prefHeightProperty().bind(content.heightProperty()
                .subtract(content.paddingProperty().getValue().getTop())
                .subtract(content.paddingProperty().getValue().getBottom())
                .subtract(titleBox.heightProperty()));
        canvasContainer.spacingProperty().set(4);

        for (var canvas : canvases) {
            canvas.setId("sorting__container");
            canvas.prefWidthProperty().bind(canvasContainer.widthProperty()
                    .subtract(canvasContainer.spacingProperty().doubleValue())
                    .divide(canvases.size()));
            canvas.prefHeightProperty().bind(canvasContainer.heightProperty());
            canvasContainer.getChildren().add(canvas);
        }

        content.getChildren().add(titleBox);
        content.getChildren().add(canvasContainer);

        getChildren().addAll(content);
    }

    public void start() {
        for (var a : animators)
            a.start();
    }

    public void pause() {
        for (var a : animators)
            a.stop();
    }

    public void stop() {
        for (var a : animators)
            a.stop();
    }

    public List<Node> options() {
        return options;
    }

    void reset() {
        stop();
        animators.clear();

        Double[] data = new Double[n];
        for (int i = 0; i < data.length; ++i) {
            // data[i] = i * (1D / n);
            data[i] = Math.random();
        }

        for (int i = 0; i < sortFns.length; i++) {
            var sortFn = sortFns[i];
            var subtitle = subtitles[i];
            canvases.get(i).getChildren().clear();
            animators.add(
                    new SortingAnimator(subtitle, canvases.get(i), sortFn.sort(Arrays.copyOf(data, data.length)), ups));
        }

        if (!paused) {
            start();
        }
    }

    void addOptions() {
        VBox optNew = new VBox();
        VBox optPause = new VBox();
        VBox optN = new VBox();
        VBox optDur = new VBox();

        Text optNewTitle = new Text();
        Text optPauseTitle = new Text();
        Text optNTitle = new Text("No. of elements");
        Text optDurTitle = new Text("Updates per second");

        Button optNewButton = new Button();
        Button optPauseButton = new Button();
        Slider optNSlider = new Slider();
        Slider optDurSlider = new Slider();

        optNSlider.setValue(n);
        optNSlider.setMin(5);
        optNSlider.setMax(100);
        optNSlider.setShowTickMarks(true);
        optNSlider.setShowTickLabels(true);
        optNSlider.setMajorTickUnit(1);
        optNSlider.setBlockIncrement(1);

        optDurSlider.setValue(ups);
        optDurSlider.setMin(1);
        optDurSlider.setMax(60);
        optDurSlider.setShowTickMarks(true);
        optDurSlider.setShowTickLabels(true);
        optDurSlider.setMajorTickUnit(1);
        optDurSlider.setBlockIncrement(1);

        optPauseButton.setText("pause");
        optNewButton.setText("reset");

        optNew.getChildren().addAll(optNewTitle, optNewButton);
        optPause.getChildren().addAll(optPauseTitle, optPauseButton);
        optN.getChildren().addAll(optNSlider, optNTitle);
        optDur.getChildren().addAll(optDurSlider, optDurTitle);

        optNewButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> reset());
        optPauseButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (paused) {
                paused = false;
                optPauseButton.setText("pause");
                start();
            } else {
                paused = true;
                optPauseButton.setText("unpause");
                stop();
            }
        });
        optNSlider.valueProperty().addListener((e, oldValue, newValue) -> this.n = newValue.intValue());
        optDurSlider.valueProperty().addListener((e, oldValue, newValue) -> {
            this.ups = newValue.intValue();
            for (var a : animators) {
                a.setUps(this.ups);
            }
        });

        options.add(optNew);
        options.add(optPause);
        options.add(optN);
        options.add(optDur);
    }

}
