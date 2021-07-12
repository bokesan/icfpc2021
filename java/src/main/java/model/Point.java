package model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Objects;

public class Point {

    private static final int MAX_STATIC = 32;
    private static final Point[] BY_COORD;

    static {
        BY_COORD = new Point[MAX_STATIC * MAX_STATIC];
        for (int x = 0; x < MAX_STATIC; x++) {
            for (int y = 0; y < MAX_STATIC; y++) {
                BY_COORD[x * MAX_STATIC + y] = new Point(x, y);
            }
        }
    }

    private final long x;
    private final long y;

    private Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public static Point of(long x, long y) {
        if (x >= 0 && x < MAX_STATIC && y >= 0 && y < MAX_STATIC) {
            return BY_COORD[(int) x * MAX_STATIC + (int) y];
        }
        return new Point(x, y);
    }

    public static Point of(JsonNode json) {
        if (!json.isArray()) {
            throw new IllegalArgumentException("JSON Array expected, but got " + json);
        }
        if (json.size() != 2) {
            throw new IllegalArgumentException("JSON array of size 2 expected, but got " + json.size() + " [" + json + "]");
        }
        return of(json.get(0).longValue(), json.get(1).longValue());
    }

    public Point translate(long dx, long dy) {
        return of(x + dx, y + dy);
    }

    public Point flipHorizontal(long axis) {
        long flippedX = 2 * axis - x;
        return of(flippedX, y);
    }

    public Point flipVertical(long axis) {
        long flippedY = 2 * axis - y;
        return of(x, flippedY);
    }

    public Point rotate90CW(Point c) {
        return of(c.x - (y - c.y), c.y + (x - c.x));
    }

    public Point rotate(Point center, int degrees) {
        double angle = degrees * Math.PI / 180.0;
        double s = Math.sin(angle);
        double c = Math.cos(angle);

        // translate point back to origin:
        long x = this.x - center.x;
        long y = this.y - center.y;

        // rotate point
        double xNew = x * c - y * s;
        double yNew = x * s + y * c;

        // translate point back:
        return of(Math.round(xNew) + center.x, Math.round(yNew) + center.y);
    }

    public static Point origin() {
        return BY_COORD[0];
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long distanceSquared(Point q) {
        return square(getX() - q.getX()) + square(getY() - q.getY());
    }

    public long distanceSquared(int x, int y) {
        return square(getX() - x) + square(getY() - y);
    }

    private static long square(long x) {
        return x * x;
    }

    public Point negate() {
        return of(-x, -y);
    }

    public boolean isEqual(int x, int y) {
        return this.x == x && this.y == y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public String toJson() {
        return toString();
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}
