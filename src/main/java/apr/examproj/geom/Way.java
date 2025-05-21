package apr.examproj.geom;

/**
 * Street
 */
public class Way extends Line {

    public String id;
    public String name;
    public String type;
    public double maxSpeed;

    public Way() {
        super();
    }

    public Way(String id, String name, String type) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String toString() {
        return String.format("Way[id: %s, name: %s, type: %s, maxSpeed: %f, nodes: %s]",
                id, name, type, maxSpeed, String.join(",", nodes.stream().map(n -> n.toString()).toList()));
    }

}
