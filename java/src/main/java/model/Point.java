package model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import java.io.IOException;
import java.util.Objects;

public class Point {

    private static final Point ORIGIN = new Point(0,0);

    private final long x;
    private final long y;

    private Point(long x, long y) {
        this.x = x;
        this.y = y;
    }

    public static Point of(long x, long y) {
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

    public static Point origin() {
        return ORIGIN;
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

    private static long square(long x) {
        return x * x;
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
