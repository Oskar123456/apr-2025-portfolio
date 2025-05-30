package apr.examproj.map;

import java.util.List;

import apr.examproj.config.ApplicationConfig;
import apr.examproj.enums.TransportationMode;
import apr.examproj.gui.GUIUtils;
import apr.examproj.gui.IGUIMapElement;
import apr.examproj.gui.Tooltip;
import apr.examproj.utils.Geometry;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polyline;

/**
 * MapEdge
 */
public class MapEdge implements IGUIMapElement {

    public String name;
    public String description;
    public MapNode src, dest;
    public TransportationMode transportationMode = TransportationMode.WALK;
    public double dist, maxSpeed;

    public MapEdge() {
    }

    public MapEdge(MapNode src, MapNode dest, double maxSpeed) {
        this.src = src;
        this.dest = dest;
        this.dist = Geometry.greatCicleDistance(src, dest);
        this.maxSpeed = maxSpeed;
    }

    public MapEdge(MapNode src, MapNode dest, MapPath path) {
        this.src = src;
        this.dest = dest;
        this.name = path.name;
        this.dist = Geometry.greatCicleDistance(src, dest);
        this.maxSpeed = path.maxSpeed;
    }

    public void setSrcDest(MapNode src, MapNode dest) {
        this.src = src;
        this.dest = dest;
        this.dist = Geometry.greatCicleDistance(src, dest);
    }

    public void setTransportationMode(TransportationMode mode) {
        this.transportationMode = mode;
    }

    /**
     * In hours.
     */
    public double getTravelTime(TransportationMode mode) {
        switch (mode) {
            case TransportationMode.DRIVE:
                return (dist / maxSpeed) / 1000;
            case TransportationMode.BIKE:
                return (dist / ApplicationConfig.getBikingSpeed()) / 1000;
            default:
                return (dist / ApplicationConfig.getWalkingSpeed()) / 1000;
        }
    }

    public double getTravelTime() {
        return getTravelTime(transportationMode);
    }

    @Override
    public void draw(MapBounds mapBounds, Pane renderPane) {
        var srcP = Geometry.toScreenCoords(Geometry.normalize(src.lat, src.lon));
        var destP = Geometry.toScreenCoords(Geometry.normalize(dest.lat, dest.lon));
        Polyline line = new Polyline(srcP.x * renderPane.getWidth(), srcP.y * renderPane.getHeight(),
                destP.x * renderPane.getWidth(), destP.y * renderPane.getHeight());
        line.setId("street-map__edge");
        Tooltip.setTooltip(line, name,
                String.format("dist: %.1fm%n%s", dist, GUIUtils.timeFormat(getTravelTime(transportationMode))),
                transportationMode.toString());
        renderPane.getChildren().add(line);
    }

    public void draw(Pane renderPane) {
        var srcP = Geometry.toScreenCoords(Geometry.normalize(src.lat, src.lon));
        var destP = Geometry.toScreenCoords(Geometry.normalize(dest.lat, dest.lon));
        Polyline line = new Polyline(srcP.x, srcP.y, destP.x, destP.y);
        line.setId("street-map__edge");
        Tooltip.setTooltip(line, name,
                String.format("dist: %.1fm%n%s", dist, GUIUtils.timeFormat(getTravelTime(transportationMode))),
                transportationMode.toString());
        renderPane.getChildren().add(line);
    }

    public Node drawNode() {
        var srcP = Geometry.toScreenCoords(Geometry.normalize(src.lat, src.lon));
        var destP = Geometry.toScreenCoords(Geometry.normalize(dest.lat, dest.lon));
        Polyline line = new Polyline(srcP.x, srcP.y, destP.x, destP.y);
        line.setId("street-map__edge");
        Tooltip.setTooltip(line, name,
                String.format("dist: %.1fm%n%s", dist, GUIUtils.timeFormat(getTravelTime(transportationMode))),
                transportationMode.toString());
        return line;
    }

    public static MapEdge findMin(List<MapEdge> edges) {
        MapEdge minEdge = null;
        double min = Double.POSITIVE_INFINITY;
        for (var edge : edges) {
            if (edge.dist < min) {
                min = edge.dist;
                minEdge = edge;
            }
        }
        return minEdge;
    }

}
