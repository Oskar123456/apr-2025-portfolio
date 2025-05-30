package apr.examproj.gui;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

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
