package apr.examproj.geom;

import java.util.ArrayList;
import java.util.List;

/**
 * Line
 */
public abstract class Line {

    List<Node> nodes;

    public Line() {
        nodes = new ArrayList<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

}
