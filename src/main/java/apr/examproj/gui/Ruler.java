package apr.examproj.gui;

import apr.examproj.config.ApplicationConfig;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Ruler
 */
public class Ruler extends HBox {

    Text text;

    static Ruler instance = new Ruler();

    public static Ruler getInstance() {
        return instance;
    }

    public Ruler() {
        setId("street-map__ruler");
        text = new Text();
        text.setId("street-map__ruler-text");
        getChildren().add(text);
    }

    public static void setText(String str) {
        instance.text.setText(str);
    }

    public void setRenderTarget(Pane renderPane) {
        renderPane.getChildren().add(this);
        prefWidthProperty().bind(renderPane.widthProperty().multiply(ApplicationConfig.rulerWidthFraction));
        translateXProperty().set(5);
        translateYProperty().set(5);
    }

}
