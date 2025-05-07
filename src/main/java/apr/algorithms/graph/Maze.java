package apr.algorithms.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import apr.datastructures.graph.Point2DI;

/**
 * Maze
 */
public class Maze {

    public int H, W;
    public char[][] grid;
    public Point2DI src, dest;
    public Map<Point2DI, Double> dists = new HashMap<>();
    public Map<Point2DI, Point2DI> srcs = new HashMap<>();
    public Set<Point2DI> visited = new HashSet<>();
    public List<Maze> snapshots = new ArrayList<>();

    static Map<Character, Double> travelCosts = Map.of(
            '.', 1D,
            'O', 3D);

    public Maze(int width, int height, Point2DI src, Point2DI dest) {
        H = height;
        W = width;
        this.src = src;
        this.dest = dest;
        this.grid = new char[H][W];
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                dists.put(new Point2DI(j, i), Double.POSITIVE_INFINITY);
            }
        }
    }

    public static Maze fromText(List<String> lines) {
        Maze maze = new Maze(lines.get(0).length(), lines.size(), null, null);

        for (int i = 0; i < maze.H; i++) {
            for (int j = 0; j < maze.W; j++) {
                char c = lines.get(i).charAt(j);
                if (c == 'S') {
                    maze.src = new Point2DI(j, i);
                    maze.grid[i][j] = '.';
                } else if (c == 'D') {
                    maze.dest = new Point2DI(j, i);
                    maze.grid[i][j] = '.';
                } else if (c == 'W') {
                    maze.dists.remove(new Point2DI(j, i));
                    maze.grid[i][j] = c;
                } else {
                    maze.grid[i][j] = c;
                }
            }
        }

        if (maze.src == null || maze.dest == null) {
            System.out.println("Invalid maze");
            return null;
        }

        // System.out.println("Maze.fromText:");
        // for (int i = 0; i < maze.H; i++) {
        // for (int j = 0; j < maze.W; j++) {
        // System.out.printf("%2c", maze.grid[i][j]);
        // }
        // System.out.println();
        // }

        return maze;
    }

    public double getCost(char c) {
        if (travelCosts.containsKey(c)) {
            return travelCosts.get(c);
        }
        return Double.POSITIVE_INFINITY;
    }

    public void snapshot() {
        snapshots.add(copy());
    }

    public Maze copy() {
        Maze copy = new Maze(W, H, new Point2DI(src.x, src.y), new Point2DI(dest.x, dest.y));
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                copy.grid[i][j] = grid[i][j];
            }
        }
        copy.dists.putAll(dists);
        copy.srcs.putAll(srcs);
        copy.visited.addAll(visited);
        copy.snapshots.addAll(snapshots);
        return copy;
    }
}
