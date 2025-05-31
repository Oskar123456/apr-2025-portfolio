package apr.examproj.gui;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * TextPanel
 */
public class TextPanel extends VBox {

    public TextPanel() {
        setId("street-map__textpanel");
        // setPrefHeight(150);
    }

    public void setTexts(String... texts) {
        getChildren().clear();
        for (var text : texts) {
            Text txt = new Text(text);
            txt.setId("street-map__textpanel-text");
            getChildren().add(txt);
        }
    }

    public void position(Pane renderPane) {
        renderPane.widthProperty().addListener(e -> reposition(renderPane));
        renderPane.heightProperty().addListener(e -> reposition(renderPane));
        widthProperty().addListener(e -> reposition(renderPane));
        heightProperty().addListener(e -> reposition(renderPane));
    }

    public void reposition(Pane renderPane) {
        relocate(renderPane.getWidth() - getWidth() - 10,
                renderPane.getHeight() - heightProperty().doubleValue() - 10);
    }
}
