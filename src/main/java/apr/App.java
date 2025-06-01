package apr;

import com.github.javafaker.Faker;

import apr.datastructures.graph.Point2D;
import apr.examproj.utils.Geometry;

/**
 * App
 */
public class App {

    static Faker faker = new Faker();

    public static void main(String[] args) {

        Point2D p1 = new Point2D(0, 1);
        Point2D p2 = new Point2D(1, 1);
        Point2D p3 = new Point2D(1, 0);

        Point2D p4 = Geometry.closestPoint(p1, p2, p3);

        System.out.printf("closest point from %s to %s <--> %s = %s%n",
                p1.toString(), p2.toString(), p3.toString(), p4.toString());

        // System.out.println(doc);

        // for (var e : hm) {
        //
        // }

        // AStarGUI.Maze maze;
        // try {
        // maze = AStarGUI.readMaze("data/maze2.txt");
        // AStarGUI.solve(maze);
        // // maze.DrawMe(gc);
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // TestDijkstra.testDijkstra();
        // TestAStar.testAStar();
    }

}
