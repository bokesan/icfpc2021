package model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Figure {

    public static class Edge {

        private final int vertex1;

        private final int vertex2;

        private Edge(int v1, int v2) {
            vertex1 = v1;
            vertex2 = v2;
        }

        private Edge(Point p) {
            vertex1 = (int) p.getX();
            vertex2 = (int) p.getY();
        }

        @Override
        public String toString() {
            return "[" + vertex1 + ", " + vertex2 + "]";
        }
    }

    private final Point[] vertices;
    private final Edge[] edges;
    private final long[] originalLengths;

    private Figure(Point[] vertices, Edge[] edges) {
        this.vertices = vertices;
        this.edges = edges;
        this.originalLengths = new long[edges.length];
        for (int i = 0; i < edges.length; i++) {
            originalLengths[i] = getEdgeLengthSquared(i);
        }
    }

    public static Figure of(Collection<Point> vertices, Collection<Point> edges) {
        return new Figure(vertices.toArray(new Point[0]), edges.stream().map(Edge::new).toArray(Edge[]::new));
    }

    public static Figure of(JsonNode json) {
        if (!json.isObject()) {
            throw new IllegalArgumentException("JSON object expected, but got " + json);
        }
        List<Point> vertices = new ArrayList<>();
        for (JsonNode vertex : json.get("vertices")) {
            vertices.add(Point.of(vertex));
        }
        List<Point> edges = new ArrayList<>();
        for (JsonNode edge : json.get("edges")) {
            edges.add(Point.of(edge));
        }
        return of(vertices, edges);
    }

    public int getNumVertices() {
        return vertices.length;
    }

    public int getNumEdges() {
        return edges.length;
    }

    public Edge getEdge(int i) {
        return edges[i];
    }

    public long getEdgeLengthSquared(int i) {
        Edge edge = edges[i];
        Point p = vertices[edge.vertex1];
        Point q = vertices[edge.vertex2];
        return p.distanceSquared(q);
    }

    public long getOriginalEdgeLengthSquared(int i) {
        return originalLengths[i];
    }

    public Point getVertex(int i) {
        return vertices[i];
    }

    public Point getEdgeStart(int i) {
        return vertices[edges[i].vertex1];
    }

    public Point getEdgeEnd(int i) {
        return vertices[edges[i].vertex2];
    }

    public Bounds getBounds() {
        return Bounds.of(vertices);
    }

    public void translate(int x, int y) {
        int n = vertices.length;
        for (int i = 0; i < n; i++) {
            vertices[i] = vertices[i].translate(x, y);
        }
    }

    public void flipHorizontal() {
        Bounds bounds = getBounds();
        long x = (bounds.getMinX() + bounds.getMaxX()) / 2;
        int n = vertices.length;
        for (int i = 0; i < n; i++) {
            vertices[i] = vertices[i].flipHorizontal(x);
        }
    }

    public void flipVertical() {
        Bounds bounds = getBounds();
        long y = (bounds.getMinY() + bounds.getMaxY()) / 2;
        int n = vertices.length;
        for (int i = 0; i < n; i++) {
            vertices[i] = vertices[i].flipVertical(y);
        }
    }

    public void rotate90CW() {
        Point center = getBounds().getCenter();
        int n = vertices.length;
        for (int i = 0; i < n; i++) {
            vertices[i] = vertices[i].rotate90CW(center);
        }
    }

    public void rotate(int degrees) {
        Point center = getBounds().getCenter();
        int n = vertices.length;
        for (int i = 0; i < n; i++) {
            vertices[i] = vertices[i].rotate(center, degrees);
        }
    }

    public void moveVertex(int index, Point newPosition) {
        vertices[index] = newPosition;
    }

    public int findVertex(long x, long y) {
        for (long delta = 0; delta < 10; delta++) {
            for (int i = 0; i < vertices.length; i++) {
                long dx = Math.abs(x - vertices[i].getX());
                long dy = Math.abs(y - vertices[i].getY());
                if (dx <= delta && dy <= delta) {
                    return i;
                }
            }
        }
        return -1;
    }

    public String getPose() {
        return "{ \"vertices\": " + Hole.toJson(vertices) + " }";
    }

    @Override
    public String toString() {
        return "Figure{" +
                "vertices=" + Arrays.toString(vertices) +
                ", edges=" + Arrays.toString(edges) +
                '}';
    }
}
