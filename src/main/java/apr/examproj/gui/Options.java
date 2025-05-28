package apr.examproj.gui;

import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * MultiSelection
 */
public class Options {

    VBox container;

    static Options instance = new Options();

    public Options() {
        container = new VBox();
        container.setId("street-map__options");
    }

    public static void hide() {
        instance.container.setVisible(false);
    }

    public static void show() {
        instance.container.setVisible(true);
    }

    public static void addOption(EventHandler<MouseEvent> eventHandler) {
        Button button = new Button();
        button.setId("street-map__options-button");
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, eventHandler);
        instance.container.getChildren().add(button);
    }

    public static void clear() {
        instance.container.getChildren().clear();
    }

    public static void setOptions(List<Button> options) {
        instance.container.getChildren().clear();
        instance.container.getChildren().addAll(options);
    }

}
