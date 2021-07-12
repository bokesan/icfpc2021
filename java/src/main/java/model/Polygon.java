package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableList;
import math.BigRational;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Polygon {

    public static final Polygon UNIT_SQUARE_ORIGIN = new Polygon(new Point[]{Point.origin(), Point.of(1,0), Point.of(1,1), Point.of(0,1)});

    private final Point[] vertices;
    private final boolean clockwise;
    private final java.awt.Polygon awtPolygon;

    private Polygon(Point[] vertices) {
        this.vertices = vertices;
        this.awtPolygon = new java.awt.Polygon();
        for (Point p : vertices) {
            awtPolygon.addPoint((int) p.getX(), (int) p.getY());
        }
        this.clockwise = isClockwise(vertices);
    }

    public static Polygon of(Collection<Point> vertices) {
        return new Polygon(vertices.toArray(new Point[0]));
    }

    public static Polygon of(Point[] vertices) {
        return new Polygon(Arrays.copyOf(vertices, vertices.length));
    }

    public static Polygon of(JsonNode json) {
        if (!json.isArray()) {
            throw new IllegalArgumentException("JSON array expected, but got " + json);
        }
        if (json.size() < 3) {
            throw new IllegalArgumentException("Hole must have at least 3 vertices, but got " + json.size());
        }
        List<Point> vertices = new ArrayList<>();
        for (JsonNode vertex : json) {
            vertices.add(Point.of(vertex));
        }
        return of(vertices);
    }

    public boolean contains(Point p) {
        for (int i = 0; i < vertices.length; i++) {
            int j = ((i == 0) ? vertices.length : i) - 1;
            if (Lines.containsPoint(vertices[i], vertices[j], p)) {
                return true;
            }
        }
        return awtPolygon.contains((int) p.getX(), (int) p.getY());
    }

    public boolean containsEdge(Point p1, Point p2) {
        if (!(contains(p1) && contains(p2))) {
            return false;
        }
        Point mid = Points.middle(p1, p2);
        if (mid != null && !contains(mid))
            return false;
        for (int i = 0; i < vertices.length; i++) {
            int j = ((i == 0) ? vertices.length : i) - 1;
            /*
            if (vertices[i].equals(p1) && vertices[j].equals(p2))
                return true;
            if (vertices[j].equals(p1) && vertices[i].equals(p2))
                return true;
            */
            if (Lines.intersect(vertices[i], vertices[j], p1, p2)) {
                return false;
            }
        }
        return true;
    }

    public long getEdgeLengthSquared(int startVertex) {
        int endVertex = (startVertex == getNumVertices() - 1) ? 0 : (startVertex + 1);
        return getVertex(startVertex).distanceSquared(getVertex(endVertex));
    }

    public static String toJson(Point[] vertices) {
        StringBuilder b = new StringBuilder();
        String sep = "[";
        for (Point vertex : vertices) {
            b.append(sep);
            sep = ", ";
            b.append(vertex.toJson());
        }
        b.append("]");
        return b.toString();
    }

    public Bounds getBounds() {
        return Bounds.of(vertices);
    }

    /**
     * DON'T mutate!
     */
    public Point[] getVertices() {
        return vertices;
    }

    public int getNumVertices() {
        return vertices.length;
    }

    public Point getVertex(int i) {
        return vertices[i];
    }

    @Override
    public String toString() {
        return "Hole: " + Arrays.toString(vertices);
    }

    public boolean isClockwise() {
        return clockwise;
    }

    private static boolean isClockwise(Point[] points) {
        return signedArea(points).signum() > 0;
    }

    public boolean isConvex() {
        int n = vertices.length;
        for (int i = 0; i < n; i++) {
            Point before = (i == 0) ? vertices[n - 1] : vertices[i - 1];
            Point middle = vertices[i];
            Point after = (i == n - 1) ? vertices[0] : vertices[i + 1];

            if (clockwise && turnsLeft(before, middle, after)) return false;
            if (!clockwise && turnsLeft(after, middle, before)) return false;
        }
        return true;
    }

    public BigRational signedArea() {
        return signedArea(vertices);
    }

    public BigRational area() {
        return signedArea().abs();
    }

    private static BigRational signedArea(Point[] points) {
        BigRational sum = BigRational.ZERO;
        for (int i = 0; i < points.length; i++) {
            int j = (i == points.length - 1) ? 0 : (i+1);
            Point p1 = points[i];
            Point p2 = points[j];
            BigRational dx = BigRational.valueOf(p2.getX() - p1.getX());
            BigRational dy = BigRational.valueOf(p2.getY() + p1.getY());
            BigRational edge = dx.multiply(dy);
            sum = sum.add(edge);
        }
        return sum.multiply(BigRational.valueOf(1,2));
    }

    protected static boolean turnsLeft(Point before, Point middle, Point after) {
        if (middle.getX() < before.getX()) {
            //if the point we are looking at is left of the point before it
            if (after.getX() < middle.getX()) {
                BigRational firstPartSlope = BigRational.valueOf(middle.getY() - before.getY(), Math.abs(before.getX() - middle.getX()));
                BigRational secondPartSlope = BigRational.valueOf(after.getY() - middle.getY(), Math.abs(middle.getY() - after.getY()));
                //make sure the slope isn't the same, because then something was wrong with the polygon in the first place
                assert !firstPartSlope.equals(secondPartSlope);
                //and the point after ours is left from us as well
                //we need to check whether the value of the slope between middle and after is lower than the one between before and middle
                return secondPartSlope.compareTo(firstPartSlope) < 0;
            } else if (after.getX() > middle.getX()) {
                BigRational firstPartSlope = BigRational.valueOf(middle.getY() - before.getY(), Math.abs(before.getX() - middle.getX()));
                BigRational secondPartSlope = BigRational.valueOf(after.getY() - middle.getY(), Math.abs(middle.getX() - after.getX()));
                //and the point after ours is right from us
                //we need to check whether the value of the slope between middle and after is smaller than the one between before and middle
                return secondPartSlope.compareTo(firstPartSlope.negate()) < 0;
            } else {
                //the point after us is at the same x position, it needs to be below us
                return after.getY() < middle.getY();
            }
        } else if (middle.getX() > before.getX()) {
            //if the point we are looking at is right of the point before it
            if (after.getX() < middle.getX()) {
                BigRational firstPartSlope = BigRational.valueOf(middle.getY() - before.getY(), Math.abs(before.getX() - middle.getX()));
                BigRational secondPartSlope = BigRational.valueOf(after.getY() - middle.getY(), Math.abs(middle.getX() - after.getX()));
                //and the point after ours is left from us
                //we need to check whether the value of the slope between middle and after is higher than the one between before and middle
                return secondPartSlope.negate().compareTo(firstPartSlope) < 0;
            } else if (after.getX() > middle.getX()) {
                BigRational firstPartSlope = BigRational.valueOf(middle.getY() - before.getY(), Math.abs(before.getX() - middle.getX()));
                BigRational secondPartSlope = BigRational.valueOf(after.getY() - middle.getY(), Math.abs(middle.getX() - after.getX()));
                //make sure the slope isn't the same, because then something was wrong with the polygon in the first place
                assert !firstPartSlope.equals(secondPartSlope);
                //and the point after ours is right from us
                //we need to check whether the value of the slope between middle and after is higher than the one between before and middle
                return secondPartSlope.compareTo(firstPartSlope) > 0;
            } else {
                //the point after us is at the same x position, it needs to be above us
                return after.getY() > middle.getY();
            }
        } else {
            //the point before us is at the same x coordinate as us
            if (before.getY() > middle.getY()) {
                //the point before is above us, so the point afte us needs to be to the right
                return (after.getX() > middle.getX());
            } else if (before.getY() < middle.getY()) {
                //the point before us is below us, so the point after needs to be left of us
                return (after.getX() < middle.getX());
            } else {
                throw new RuntimeException("Three polygon points on same x coordinate");
            }
        }
    }

    public boolean containsVertex(Point p) {
        for (Point v : vertices) {
            if (v.equals(p)) {
                return true;
            }
        }
        return false;
    }

}
