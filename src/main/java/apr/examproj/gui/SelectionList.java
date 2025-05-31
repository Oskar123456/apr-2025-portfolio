package apr.examproj.gui;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * SelectionList
 */
public class SelectionList extends VBox {

    public SelectionList() {
        setId("street-map__selection-list");
    }

    public void addButton(EventHandler<MouseEvent> eventHandler, String text) {
        Button button = new Button();
        button.setId("street-map__button");
        button.setText(text);
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, eventHandler);
        getChildren().add(button);
    }

}
