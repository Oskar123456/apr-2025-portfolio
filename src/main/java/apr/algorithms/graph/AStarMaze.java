package apr.algorithms.graph;

import java.util.PriorityQueue;

import apr.datastructures.graph.Point2DI;
import apr.datastructures.graph.TwoTuple;

/**
 * AStarMaze
 */
public class AStarMaze {

    public static void solveAllowDiag(Maze maze, HeuristicFunction heuristicFct) {
        maze.dists.put(maze.src, 0D);
        var PQ = new PriorityQueue<TwoTuple<Point2DI, Double>>((a, b) -> a.second.compareTo(b.second));
        PQ.add(new TwoTuple<Point2DI, Double>(maze.src, 0D));

        while (!PQ.isEmpty()) {
            var curTuple = PQ.remove();
            var curPoint = curTuple.first;
            var curDist = maze.dists.get(curPoint);
            maze.visited.add(curPoint);
            maze.snapshot();
            if (curPoint.equals(maze.dest)) {
                break;
            }
            for (var dir : Point2DI.dirsIncDiag) {
                var neighborPoint = curPoint.add(dir);
                if (!maze.dists.containsKey(neighborPoint)) {
                    continue;
                }
                var neighborDist = maze.dists.get(neighborPoint);
                var newDist = curDist + maze.getCost(maze.grid[neighborPoint.y][neighborPoint.x]);
                if (neighborDist > newDist) {
                    maze.dists.put(neighborPoint, newDist);
                    maze.srcs.put(neighborPoint, curPoint);
                    PQ.add(new TwoTuple<Point2DI, Double>(neighborPoint,
                            newDist + heuristicFct.estimate(neighborPoint, maze.dest)));
                }
            }
        }
    }

    public static void solve(Maze maze, HeuristicFunction heuristicFct) {
        maze.dists.put(maze.src, 0D);
        var PQ = new PriorityQueue<TwoTuple<Point2DI, Double>>((a, b) -> a.second.compareTo(b.second));
        PQ.add(new TwoTuple<Point2DI, Double>(maze.src, 0D));

        while (!PQ.isEmpty()) {
            var curTuple = PQ.remove();
            var curPoint = curTuple.first;
            var curDist = maze.dists.get(curPoint);
            maze.visited.add(curPoint);
            maze.snapshot();
            if (curPoint.equals(maze.dest)) {
                break;
            }
            for (var dir : Point2DI.dirs) {
                var neighborPoint = curPoint.add(dir);
                if (!maze.dists.containsKey(neighborPoint)) {
                    continue;
                }
                var neighborDist = maze.dists.get(neighborPoint);
                var newDist = curDist + maze.getCost(maze.grid[neighborPoint.y][neighborPoint.x]);
                if (neighborDist > newDist) {
                    maze.dists.put(neighborPoint, newDist);
                    maze.srcs.put(neighborPoint, curPoint);
                    PQ.add(new TwoTuple<Point2DI, Double>(neighborPoint,
                            newDist + heuristicFct.estimate(neighborPoint, maze.dest)));
                }
            }
        }
    }

    public interface HeuristicFunction {
        public double estimate(Point2DI src, Point2DI dest);
    }
}
