package math;

public class Vector2 {

    public static final Vector2 ZERO = new Vector2(0, 0);

    double x;
    double y;

    private Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public static Vector2 of(double x, double y) {
        return new Vector2(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public Vector2 negate() {
        return scale(-1);
    }

    public Vector2 scale(double c) {
        return of(c * x, c * y);
    }

    public Vector2 add(Vector2 v) {
        return of(x + v.x, y + v.y);
    }

    public Vector2 subtract(Vector2 v) {
        return of(x - v.x, y - v.y);
    }

    public double dot(Vector2 v) {
        return x * v.x + y * v.y;
    }
}
