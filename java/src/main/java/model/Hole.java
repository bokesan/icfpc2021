package model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Hole {

    private final Point[] vertices;

    private Hole(Collection<Point> vertices) {
        this.vertices = vertices.toArray(new Point[vertices.size()]);
    }

    public static Hole of(Collection<Point> vertices) {
        return new Hole(vertices);
    }

    public static Hole of(JsonNode json) {
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


    @Override
    public String toString() {
        return "Hole: " + Arrays.toString(vertices);
    }

}
