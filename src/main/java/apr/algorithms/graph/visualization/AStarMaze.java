package apr.algorithms.graph.visualization;

import java.util.PriorityQueue;

import apr.datastructures.graph.Point2DI;
import apr.datastructures.graph.TwoTuple;

/**
 * AStarMaze
 */
public class AStarMaze {

    static double tieThreshold = 0.001;

    public static void solveAllowDiag(Maze maze, HeuristicFunction heuristicFct) {
        maze.dists.put(maze.src, 0D);
        var PQ = new PriorityQueue<TwoTuple<Point2DI, Double>>((a, b) -> {
            if (Math.abs(a.second.compareTo(b.second)) <= tieThreshold) {
                double aDist = a.first.dist(maze.dest);
                double bDist = b.first.dist(maze.dest);
                if (aDist < bDist) {
                    return -1;
                } else {
                    return 1;
                }
            }
            return a.second.compareTo(b.second);
        });
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
                var newDist = curDist + maze.travelCost(curPoint, dir);
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
                var newDist = curDist + maze.travelCost(curPoint, dir);
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
