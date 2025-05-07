package apr.algorithms.graph.visualization;

import java.util.List;

import apr.algorithms.graph.Maze;
import apr.datastructures.graph.Point2DI;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * AStarLooper
 */
public class AStarAnimator extends AnimationTimer {

    Canvas canvas;
    GraphicsContext gfxCtx;
    List<Maze> mazes;
    long duration, lastUpdate, pauseLeft, updateFreq, lastFrame;
    int curMazeIdx, mazeSnapshotIdx;

    public AStarAnimator(GraphicsContext graphicsContext, long durationInSeconds, List<Maze> solvedMazes) {
        this.canvas = graphicsContext.getCanvas();
        this.gfxCtx = graphicsContext;
        this.duration = durationInSeconds * 1000000000;
        this.mazes = solvedMazes;
    }

    public void handle(long tnow) {
        this.updateFreq = duration / mazes.get(curMazeIdx).snapshots.size();
        if (pauseLeft > 0) {
            pauseLeft -= tnow - lastFrame;
            lastFrame = tnow;
            return;
        }
        if (!(lastUpdate == 0) && tnow - lastUpdate < updateFreq) {
            return;
        }

        lastUpdate = tnow;

        draw();

        mazeSnapshotIdx++;
        if (mazeSnapshotIdx >= mazes.get(curMazeIdx).snapshots.size()) {
            pauseLeft = 2000000000;
            lastFrame = tnow;
            curMazeIdx = (curMazeIdx + 1) % mazes.size();
            mazeSnapshotIdx = 0;
            // this.stop();
        }
    }

    void draw() {
        clear();

        Maze curMaze = mazes.get(curMazeIdx).snapshots.get(mazeSnapshotIdx);

        double padding = Math.min(canvas.getWidth() / 50.0, canvas.getHeight() / 50.0);
        double cellW = (canvas.getWidth() - padding * 2) / curMaze.W;
        double cellH = (canvas.getHeight() - padding * 2) / curMaze.H;

        gfxCtx.setStroke(Color.BLACK);
        for (int i = 0; i < curMaze.H; i++) {
            for (int j = 0; j < curMaze.W; j++) {
                Point2DI curPoint = new Point2DI(j, i);
                double x = padding + j * cellW, y = padding + i * cellH;

                if (curPoint.equals(curMaze.src)) {
                    gfxCtx.setFill(Color.GREEN);
                } else if (curPoint.equals(curMaze.dest)) {
                    gfxCtx.setFill(Color.RED);
                } else if (curMaze.visited.contains(curPoint)) {
                    gfxCtx.setFill(Color.GRAY);
                } else if (curMaze.dists.get(curPoint) != Double.POSITIVE_INFINITY) {
                    gfxCtx.setFill(Color.LIGHTSLATEGRAY);
                } else if (curMaze.grid[i][j] == 'W') {
                    gfxCtx.setFill(Color.BLACK);
                } else {
                    gfxCtx.setFill(Color.WHITE);
                }

                if (curMaze.grid[i][j] == 'O') {
                    gfxCtx.setFill(gfxCtx.getFill().interpolate(Color.BLUE, 0.7));
                }

                gfxCtx.strokeRect(padding + j * cellW, padding + i * cellH, cellW, cellH);
                gfxCtx.fillRect(padding + j * cellW, padding + i * cellH, cellW, cellH);

                if (curMaze.dists.get(curPoint) != Double.POSITIVE_INFINITY) {
                    gfxCtx.setFill(Color.BLACK);
                    gfxCtx.fillText(String.format("%.1f", curMaze.dists.get(curPoint)), x, y + cellH, cellW * 0.9);
                }
            }
        }
    }

    void clear() {
        gfxCtx.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
}
