package apr.examproj.driver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import apr.examproj.ds.Graph;
import apr.examproj.map.MapNode;
import apr.examproj.map.MapPath;
import apr.examproj.map.StreetMap;
import apr.examproj.osm.MapData;
import javafx.scene.layout.Pane;

/**
 * StreetMapDriver
 */
public class StreetMapDriver {

    StreetMap streetMap;
    MapNode src, dest;
    MapPath path;

    Graph<MapNode> graph;

    /* GUI */

    public void start(Pane renderPane) throws IOException {
        String osmStr = new String(Files.readAllBytes(Paths.get("data/map.osm")));
        MapData mapData = new MapData(osmStr);
        streetMap = new StreetMap(mapData);
        streetMap.setRenderTarget(renderPane);
        System.out.println("StreetMapDriver.start()");
        System.out.println(streetMap);
    }

}
