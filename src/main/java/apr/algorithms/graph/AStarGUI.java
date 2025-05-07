package apr.algorithms.graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import apr.datastructures.graph.Point2DI;
import apr.datastructures.graph.TwoTuple;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * AStarGUI
 */
public class AStarGUI {

    public static void runMazes(GraphicsContext graphicsContext, String dir) {
        AStarGUI.Maze maze;
        try {
            File directory = new File("data");

            Timmy timer = new Timmy();
            timer.mazesList = new ArrayList<>();
            timer.gc = graphicsContext;

            System.out.println("AStarGUI running the following mazes:");
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    continue;
                }
                if (!file.getName().startsWith("maze")) {
                    continue;
                }
                maze = AStarGUI.readMaze(file.getAbsolutePath());
                List<AStarGUI.Maze> mazes = AStarGUI.solve(maze);
                timer.mazesList.add(mazes);

                System.out.printf("\t%s%n", file.getAbsolutePath());
            }

            timer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Maze readMaze(String filePath) throws IOException {
        var lines = Files.readAllLines(Paths.get(filePath));
        int h = lines.size(), w = lines.get(0).length();
        if (h < 1 || w < 1) {
            System.out.println("LOL");
            return null;
        }

        Point2DI start = null, end = null;
        char[][] maze = new char[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                maze[i][j] = lines.get(i).charAt(j);
                if (maze[i][j] == 'S') {
                    start = new Point2DI(j, i);
                }
                if (maze[i][j] == 'E') {
                    end = new Point2DI(j, i);
                }
            }
        }

        // for (int i = 0; i < h; i++) {
        // for (int j = 0; j < w; j++) {
        // System.out.printf("%2c", maze[i][j]);
        // }
        // System.out.println();
        // }

        return new Maze(maze, start, end);
    }

    public static List<Maze> solve(Maze maze) {
        Map<Point2DI, Double> dists = new HashMap<>();
        Map<Point2DI, Point2DI> srcs = new HashMap<>();
        Point2DI src = maze.start;
        Point2DI dest = maze.end;

        List<Maze> snapShots = new ArrayList<>();

        for (int i = 0; i < maze.H; i++) {
            for (int j = 0; j < maze.W; j++) {
                dists.put(new Point2DI(j, i), Double.POSITIVE_INFINITY);
            }
        }
        dists.put(src, 0D);

        PriorityQueue<TwoTuple<Point2DI, Double>> PQ = new PriorityQueue<>((a, b) -> a.second.compareTo(b.second));
        PQ.add(new TwoTuple<Point2DI, Double>(src, 0D));

        while (!PQ.isEmpty()) {
            var curTuple = PQ.remove();
            var curPoint = curTuple.first;
            var curDist = dists.get(curPoint);
            if (curPoint.equals(dest)) {
                break;
            }
            for (Point2DI dir : Point2DI.dirs) {
                Point2DI neighborPoint = curPoint.add(dir);
                if (!dists.containsKey(neighborPoint)) {
                    continue;
                }
                if (maze.grid[neighborPoint.y][neighborPoint.x] == 'W') {
                    continue;
                }
                var neighborDist = dists.get(neighborPoint);
                if (neighborDist > curDist + 1) {
                    dists.put(neighborPoint, curDist + 1);
                    srcs.put(neighborPoint, curPoint);
                    PQ.add(new TwoTuple<Point2DI, Double>(neighborPoint, curDist + 1 + neighborPoint.dist(dest)));
                    for (var entry : dists.entrySet()) {
                        maze.dists[entry.getKey().y][entry.getKey().x] = entry.getValue();
                    }
                    snapShots.add(maze.copy());
                }
            }
        }

        for (var entry : dists.entrySet()) {
            maze.dists[entry.getKey().y][entry.getKey().x] = entry.getValue();
        }

        Point2DI p = dest;
        while (srcs.get(p) != null) {
            maze.path.add(srcs.get(p));
            p = srcs.get(p);
        }

        return snapShots;
    }

    public static class Timmy extends AnimationTimer {

        public long solveDuration = 3000000000L;
        public long lastUpdate = 0, updateFreq = 40000000L;
        public GraphicsContext gc;
        public List<List<Maze>> mazesList;
        public List<Maze> mazes;
        public int mazesListIdx = 0;
        public int mazesIdx = 0;

        @Override
        public void handle(long now) {
            updateFreq = solveDuration / mazesList.get(mazesListIdx).size();
            if (mazesIdx >= mazesList.get(mazesListIdx).size()) {
                gc.fillText("DONE!", 400, 400, 200);
                mazesListIdx = (mazesListIdx + 1) % mazesList.size();
                mazesIdx = 0;
                lastUpdate += 1000000000;
                return;
            }
            if (!(lastUpdate == 0) && now - lastUpdate > 0 && now - lastUpdate < updateFreq) {
                return;
            }
            Maze m = mazesList.get(mazesListIdx).get(mazesIdx);
            lastUpdate = now;
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, 800, 800);
            gc.setFill(Color.PINK);
            m.DrawMe(gc);
            mazesIdx++;
        }

    }

    public static class Maze {

        public final char[][] grid;
        public double[][] dists;
        public Point2DI start, end;
        public Set<Point2DI> path;
        public int H, W;

        public Maze(char[][] grid, Point2DI start, Point2DI end) {
            this.grid = grid;
            this.start = start;
            this.end = end;
            W = grid[0].length;
            H = grid.length;
            path = new HashSet<>();
            this.dists = new double[H][W];
        }

        public Maze copy() {
            char[][] gridCopy = new char[H][W];
            double[][] distsCopy = new double[H][W];
            for (int i = 0; i < H; i++) {
                for (int j = 0; j < W; j++) {
                    gridCopy[i][j] = grid[i][j];
                    distsCopy[i][j] = dists[i][j];
                }
            }
            Maze m = new Maze(gridCopy, start, end);
            m.dists = distsCopy;
            for (var p : path) {
                m.path.add(p);
            }
            return m;
        }

        public void DrawMe(GraphicsContext gc) {
            double padding = 10;
            double cellH = (gc.getCanvas().heightProperty().doubleValue() - padding * 2) / H;
            double cellW = (gc.getCanvas().widthProperty().doubleValue() - padding * 2) / W;

            for (int i = 0; i < H; i++) { // TODO: use Text object for texting
                for (int j = 0; j < W; j++) {
                    double x = padding + j * cellW;
                    double y = padding + i * cellH;

                    char c = grid[i][j];
                    Point2DI p = new Point2DI(j, i);

                    if (dists[i][j] != Double.POSITIVE_INFINITY) {
                        gc.setFill(Color.LIGHTSLATEGRAY);
                        gc.fillRect(padding + j * cellW, padding + i * cellH, cellW, cellH);
                    }

                    if (path.contains(p)) {
                        gc.setFill(Color.GRAY);
                        gc.fillRect(padding + j * cellW, padding + i * cellH, cellW, cellH);
                    }
                    if (i == start.y && j == start.x) {
                        gc.setFill(Color.GREEN);
                        gc.fillRect(padding + j * cellW, padding + i * cellH, cellW, cellH);
                        gc.setFill(Color.DARKRED);
                        gc.fillText("SRC", x, y + cellH / 2, cellW);
                    } else if (i == end.y && j == end.x) {
                        gc.setFill(Color.YELLOW);
                        gc.fillRect(padding + j * cellW, padding + i * cellH, cellW, cellH);
                        gc.setFill(Color.DARKRED);
                        gc.fillText("DEST", x, y + cellH / 2, cellW);
                    } else if (c == 'W') {
                        gc.setFill(Color.BLACK);
                        gc.fillRect(padding + j * cellW, padding + i * cellH, cellW, cellH);
                    } else if (c == '.') {
                        gc.setStroke(Color.BLACK);
                        gc.strokeRect(padding + j * cellW, padding + i * cellH, cellW, cellH);
                    }

                    String distStr = (dists[i][j] == Double.POSITIVE_INFINITY) ? ""
                            : String.format("%.1f", dists[i][j]);

                    gc.setFill(Color.DARKMAGENTA);
                    gc.fillText(distStr, x, y + cellH, cellW);
                }
            }
        }
    }
}
