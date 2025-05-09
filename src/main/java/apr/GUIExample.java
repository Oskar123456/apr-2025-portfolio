package apr;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * GUIExample
 */
public abstract class GUIExample extends Pane {

    public abstract void start();

    public abstract void pause();

    public abstract void stop();

    public abstract List<Node> options();
}
