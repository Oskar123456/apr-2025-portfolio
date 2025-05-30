package apr.examproj.gui;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * ToolPanel
 */
public class ToolPanel extends HBox {

    public ToolPanel() {
        setId("street-map__toolpanel");
        setPrefHeight(65);
    }

    public void addButton(EventHandler<MouseEvent> eventHandler, String text) {
        Button button = new Button();
        button.setId("street-map__toolpanel-button");
        button.setText(text);
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            eventHandler.handle(e);
        });
        getChildren().add(button);
    }

    public void addSlider(String title, long min, long max, long init,
            ChangeListener<? super Number> changeListener) {
        VBox vbox = new VBox();
        vbox.setId("street-map__slider-box");
        Text txt = new Text(String.format(title, init));
        txt.setId("street-map__slider-text");
        Slider slider = new Slider();
        slider.setId("street-map__slider");
        slider.setValue(init);
        slider.setMin(min);
        slider.setMax(max);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit((max - min) / 10);
        slider.setBlockIncrement((max - min) / 100);
        slider.valueProperty().addListener(changeListener);
        slider.valueProperty().addListener((e, o, n) -> txt.setText(String.format(title, n.longValue())));
        vbox.getChildren().addAll(txt, slider);
        getChildren().add(vbox);
    }

    public void addChoiceBox(ChangeListener<String> eventHandler, List<String> titles) {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        for (int i = 0; i < titles.size(); ++i) {
            var title = titles.get(i);
            choiceBox.getItems().add(title);
        }
        choiceBox.valueProperty().addListener(eventHandler);
        choiceBox.valueProperty().set(titles.get(0));
        getChildren().add(choiceBox);
    }

    public void position(Pane renderPane) {
        renderPane.widthProperty().addListener(e -> reposition(renderPane));
        renderPane.heightProperty().addListener(e -> reposition(renderPane));
    }

    public void reposition(Pane renderPane) {
        relocate(10, renderPane.getHeight() - prefHeightProperty().doubleValue() - 10);
    }

}
