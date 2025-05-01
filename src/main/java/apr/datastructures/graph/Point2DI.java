package apr.datastructures.graph;

/**
 * Point2DI
 */
public class Point2DI {

    public static final Point2DI[] dirs = {
            new Point2DI(-1, 0),
            new Point2DI(0, 1),
            new Point2DI(1, 0),
            new Point2DI(0, -1),
    };

    public int x, y;

    public Point2DI(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int dist(Point2DI other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Point2DI add(Point2DI other) {
        return new Point2DI(x + other.x, y + other.y);
    }

    public String toString() {
        return String.format("Point2DI[%d, %d]", x, y);
    }
}
