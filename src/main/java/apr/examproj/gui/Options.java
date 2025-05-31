package apr.examproj.gui;

import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * MultiSelection
 */
public class Options extends VBox {

    static Options instance = new Options();

    public Options() {
        setId("street-map__options");
        setPrefWidth(100);
    }

    public static void hide() {
        instance.setVisible(false);
    }

    public static void show() {
        instance.setVisible(true);
    }

    public static void addOption(EventHandler<MouseEvent> eventHandler, String text) {
        Button button = new Button();
        button.setId("street-map__options-button");
        button.setText(text);
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            eventHandler.handle(e);
            hide();
        });
        button.prefWidthProperty().bind(instance.widthProperty()
                .subtract(instance.paddingProperty().getValue().getRight())
                .subtract(instance.paddingProperty().getValue().getLeft()));
        instance.getChildren().add(button);
    }

    public static Options getInstance() {
        return instance;
    }

    public static void clear() {
        instance.getChildren().clear();
    }

    public static void setOptions(List<Button> options) {
        instance.getChildren().clear();
        instance.getChildren().addAll(options);
    }

    public static void setPosition(double x, double y) {
        instance.relocate(x, y);
    }

    public void setRenderTarget(Pane renderPane) {
        renderPane.getChildren().add(this);
    }

}
