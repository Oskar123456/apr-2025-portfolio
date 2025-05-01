package apr.datastructures;

/**
 * Point2D
 */
public class Point2D extends Point {
    double x, y;

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

    public String toString() {
        return String.format("Point2D[%.1f, %.1f]", x, y);
    }
}
