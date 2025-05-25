package apr.examproj.gui;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Tooltip
 */
public class Tooltip extends VBox {

    public HBox titleBox;
    public VBox contentBox;
    public HBox footnoteBox;
    public Text footnoteText;
    public Text titleText;

    static Tooltip instance = new Tooltip();

    public Tooltip() {
        setId("street-map__tooltip");
        prefWidth(100);
        prefHeight(100);

        titleBox = new HBox();
        titleBox.setId("street-map__tooltip-title-box");
        contentBox = new VBox();
        contentBox.setId("street-map__tooltip-content-box");
        footnoteBox = new HBox();
        footnoteBox.setId("street-map__tooltip-footnote-box");
        titleText = new Text();
        titleText.setId("street-map__tooltip-title-text");
        titleBox.getChildren().add(titleText);
        footnoteText = new Text();
        footnoteText.setId("street-map__tooltip-footnote-text");
        footnoteBox.getChildren().add(footnoteText);
        getChildren().addAll(titleBox, contentBox, footnoteBox);
        setVisible(false);
    }

    public Tooltip(String title, String content, String footnote) {
        this();
        setTitle(title);
        addContent(new Text(content));
        setFootnote(footnote);
    }

    public static Tooltip getInstance() {
        return instance;
    }

    public void setRenderTarget(Pane renderPane) {
        renderPane.getChildren().add(this);
        renderPane.addEventHandler(MouseEvent.MOUSE_MOVED, (e) -> {
            relocate(e.getX() + 45, e.getY() - 45);
        });
    }

    public void clearContent() {
        contentBox.getChildren().clear();
    }

    public void addContent(Node node) {
        contentBox.getChildren().add(node);
    }

    public void setContent(Node node) {
        contentBox.getChildren().clear();
        contentBox.getChildren().add(node);
    }

    public void setContentText(String text) {
        contentBox.getChildren().clear();
        contentBox.getChildren().add(new Text(text));
    }

    public void setTitle(String text) {
        titleText.setText(text);
    }

    public void setFootnote(String text) {
        footnoteText.setText(text);
    }

}
