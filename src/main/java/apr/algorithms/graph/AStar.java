package apr.algorithms.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import apr.datastructures.graph.AStarGraph;
import apr.datastructures.graph.AStarNode;
import apr.datastructures.graph.Point2DI;
import apr.datastructures.graph.TwoTuple;

/**
 * AStar
 */
public class AStar {

    public static <T> AStarGraphPath<T> use(AStarGraph<T> graph, AStarNode<T> src, AStarNode<T> dest) {
        System.out.printf("%n%nAStar:: %s --> %s", src.toString(), dest.toString());

        Map<AStarNode<T>, Double> dists = new HashMap<>();
        Map<AStarNode<T>, AStarNode<T>> srcs = new HashMap<>();

        for (var node : graph.nodes) {
            dists.put(node, Double.POSITIVE_INFINITY);
        }
        dists.put(src, 0D);

        PriorityQueue<TwoTuple<AStarNode<T>, Double>> PQ = new PriorityQueue<>((a, b) -> a.second.compareTo(b.second));
        PQ.add(new TwoTuple<AStarNode<T>, Double>(src, src.dist(dest)));

        while (!PQ.isEmpty()) {
            var curTuple = PQ.remove();
            var curNode = curTuple.first;
            var curDist = dists.get(curNode);
            if (curNode == dest) {
                System.out.printf("%n%n >>> AStar:: Path with length %.1f found:%n\t", dists.get(dest));
                String str = "";
                AStarNode<T> node = curNode;
                while (curNode != null) {
                    str += curNode.toString() + " <- ";
                    curNode = srcs.get(curNode);
                }
                System.out.println(str);
                System.out.println();
                return null;
            }
            for (var edge : curNode.edges) {
                var neighbor = edge.dest;
                var newDist = curDist + curNode.dist(neighbor);
                var oldDist = dists.get(neighbor);
                if (oldDist > newDist) {
                    dists.put(neighbor, newDist);
                    srcs.put(neighbor, curNode);
                    PQ.add(new TwoTuple<AStarNode<T>, Double>(neighbor, newDist + neighbor.dist(dest)));
                }
            }
        }

        System.out.printf("%n%n >>> AStar:: No path found...%n%n");
        return null;
    }

    public static void grid(int[][] grid, Point2DI src, Point2DI dest) {
        System.out.printf("%n%nAStar::grid : %s --> %s%n", src.toString(), dest.toString());

        int w = grid[0].length;
        int h = grid.length;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                grid[y][x] = -1;
            }
        }
        grid[src.y][src.x] = 0;

        Point2DI[][] srcs = new Point2DI[h][w];

        PriorityQueue<TwoTuple<Point2DI, Integer>> PQ = new PriorityQueue<>((a, b) -> a.second.compareTo(b.second));
        PQ.add(new TwoTuple<Point2DI, Integer>(src, src.dist(dest)));

        while (!PQ.isEmpty()) {
            var curTuple = PQ.remove();
            var curPoint = curTuple.first;
            int curDist = grid[curPoint.y][curPoint.x];
            if (curPoint.x == dest.x && curPoint.y == dest.y) {
                String str = curPoint.toString();
                Point2DI p = srcs[curPoint.y][curPoint.x];
                while (p != null) {
                    str += " <-- " + p.toString();
                    p = srcs[p.y][p.x];
                }
                System.out.printf("%nPATH: %s%n", str);
                break;
            }
            for (Point2DI dir : Point2DI.dirs) {
                Point2DI neighborPos = curPoint.add(dir);
                if (!isInBounds(grid, neighborPos)) {
                    continue;
                }
                if (grid[neighborPos.y][neighborPos.x] < 0 || grid[neighborPos.y][neighborPos.x] > curDist + 1) {
                    grid[neighborPos.y][neighborPos.x] = curDist + 1;
                    srcs[neighborPos.y][neighborPos.x] = curPoint;
                    PQ.add(new TwoTuple<Point2DI, Integer>(neighborPos, curDist + 1 + neighborPos.dist(dest)));
                }
            }
        }

        printGrid(grid, srcs, src, dest);
    }

    public static void printGrid(int[][] grid, Point2DI[][] path, Point2DI src, Point2DI dest) {
        int w = grid[0].length;
        int h = grid.length;
        for (int y = -1; y < h; y++) {
            for (int x = -1; x < w; x++) {

                if (y < 0 && x < 0) {
                    System.out.printf("    ", x);
                    continue;
                }

                if (y < 0) {
                    System.out.printf("   %2d  ", x);
                    continue;
                }

                if (x < 0) {
                    System.out.printf("%2d | ", y);
                    continue;
                }

                boolean isOnPath = false;

                Point2DI p = path[dest.y][dest.x];
                while (p != null) {
                    if (p.x == x && p.y == y) {
                        isOnPath = true;
                        break;
                    }
                    p = path[p.y][p.x];
                }

                if (y == src.y && x == src.x) {
                    System.out.printf(" [S%2d] ", grid[y][x]);
                } else if (y == dest.y && x == dest.x) {
                    System.out.printf(" [D%2d] ", grid[y][x]);
                } else if (isOnPath) {
                    System.out.printf(" [X%2d] ", grid[y][x]);
                } else {
                    System.out.printf(" [ %2d] ", grid[y][x]);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static boolean isInBounds(int[][] grid, Point2DI p) {
        return p.x >= 0 && p.y >= 0 && p.x < grid[0].length && p.y < grid.length;
    }
}
