package apr;

import java.io.IOException;

import apr.algorithms.graph.AStarGUI;

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
