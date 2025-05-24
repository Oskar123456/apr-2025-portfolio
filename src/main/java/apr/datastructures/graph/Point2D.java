package apr.datastructures.graph;

/**
 * Point2D
 */
public class Point2D extends Point {

    public static final Point2D[] dirs = {
            new Point2D(-1, 0),
            new Point2D(0, 1),
            new Point2D(1, 0),
            new Point2D(0, -1),
    };

    public double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double dist(Point other) {
        Point2D p = (Point2D) other;
        return Math.sqrt((this.x - p.x) * (this.x - p.x) + (this.y - p.y) * (this.y - p.y));
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Point2D add(Point2D other) {
        return new Point2D(x + other.x, y + other.y);
    }

    public Point2D subtract(Point2D other) {
        return new Point2D(x - other.x, y - other.y);
    }

    public Point2D scale(double scale) {
        this.x *= scale;
        this.y *= scale;
        return this;
    }

    public Point2D scale(double scaleX, double scaleY) {
        this.x *= scaleX;
        this.y *= scaleY;
        return this;
    }

    public Point2D normalize() {
        double len = magnitude();
        this.x /= len;
        this.y /= len;
        return this;
    }

    public boolean equals(Object other) {
        return other != null && this.x == ((Point2D) other).x && this.y == ((Point2D) other).y;
    }

    public String toString() {
        return String.format("Point2D[%.1f, %.1f]", x, y);
    }
}
