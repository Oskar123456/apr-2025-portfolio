package apr;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import apr.algorithms.graph.AStarGUI;
import apr.datastructures.graph.Point2DI;
import apr.test.graph.TestDijkstra;

/**
 * App
 */
public class App {

    public static void main(String[] args) {
        AStarGUI.Maze maze;
        try {
            maze = AStarGUI.readMaze("data/maze2.txt");
            AStarGUI.solve(maze);
            // maze.DrawMe(gc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TestDijkstra.testDijkstra();
        // TestAStar.testAStar();
    }

}
