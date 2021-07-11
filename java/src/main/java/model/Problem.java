package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import util.ArrayUtils;

import java.io.File;
import java.io.IOException;

public class Problem {

    private final Polygon hole;
    private final Figure figure;
    private final long epsilon;
    private final double epsilonMil;
    private final long[] maxLength;
    private final long[] minLength;

    private String name;

    public Problem(Polygon hole, Figure figure, long epsilon) {
        this.hole = hole;
        this.figure = figure;
        this.epsilon = epsilon;
        this.epsilonMil = (double) epsilon / 1000000;
        int n = figure.getNumEdges();
        this.maxLength = new long[n];
        this.minLength = new long[n];
        for (int i = 0; i < n; i++) {
            long o = figure.getOriginalEdgeLengthSquared(i);
            maxLength[i] = (long) Math.floor((epsilonMil + 1) * o);
            minLength[i] = (long) Math.ceil((1 - epsilonMil) * o);
        }
    }

    public static Problem of(JsonNode node) {
        Polygon hole = Polygon.of(node.get("hole"));
        Figure figure = Figure.of(node.get("figure"));
        long epsilon = node.get("epsilon").longValue();
        return new Problem(hole, figure, epsilon);
    }

    public static Problem loadFromFile(String problemFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(new File(problemFile));
        Problem problem = of(json);
        problem.name = baseName(problemFile);
        return problem;
    }

    public Polygon getHole() {
        return hole;
    }

    public Figure getFigure() {
        return figure;
    }

    public long getEpsilon() {
        return epsilon;
    }

    public Bounds getBounds() {
        return hole.getBounds().union(figure.getBounds());
    }

    public String getName() {
        return name;
    }

    public boolean isValid() {
        int n = getFigure().getNumEdges();
        for (int i = 0; i < n; i++) {
            Point p1 = figure.getEdgeStart(i);
            Point p2 = figure.getEdgeEnd(i);
            if (!getHole().containsEdge(p1, p2)) {
                // System.out.format("hole does not contain edge %s-%s\n", p1, p2);
                return false;
            }
            long length = figure.getEdgeLengthSquared(i);
            long orig = figure.getOriginalEdgeLengthSquared(i);
            if (Math.abs((double) length / orig - 1) > epsilonMil) {
                // System.out.format("edge length wrong %s-%s\n", p1, p2);
                return false;
            }
        }
        return true;
    }

    private static boolean shouldCheck(Figure.Edge edge, int newVertexIndex) {
        return (edge.getVertex1() == newVertexIndex && edge.getVertex2() < newVertexIndex) ||
                (edge.getVertex2() == newVertexIndex && edge.getVertex1() < newVertexIndex);
    }

    public boolean isValidUpTo(int vertexIndex) {
        int n = getFigure().getNumEdges();
        for (int i = 0; i < n; i++) {
            Figure.Edge edge = figure.getEdge(i);
            if (shouldCheck(edge, vertexIndex)) {
                long length = figure.getEdgeLengthSquared(i);
                if (length < minLength[i] || length > maxLength[i]) {
                    return false;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            Figure.Edge edge = figure.getEdge(i);
            if (shouldCheck(edge, vertexIndex)) {
                Point p1 = figure.getEdgeStart(i);
                Point p2 = figure.getEdgeEnd(i);
                if (!getHole().containsEdge(p1, p2)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValidFor(int[] vertexIndices) {
        int n = getFigure().getNumEdges();
        for (int i = 0; i < n; i++) {
            Figure.Edge edge = figure.getEdge(i);
            if (shouldCheck(edge, vertexIndices)) {
                long length = figure.getEdgeLengthSquared(i);
                if (length < minLength[i] || length > maxLength[i]) {
                    return false;
                }
                Point p1 = figure.getVertex(edge.getVertex1());
                Point p2 = figure.getVertex(edge.getVertex2());
                if (!getHole().containsEdge(p1, p2)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean insideIsValidFor(int[] vertexIndices) {
        int n = getFigure().getNumEdges();
        for (int i = 0; i < n; i++) {
            Figure.Edge edge = figure.getEdge(i);
            if (shouldCheck(edge, vertexIndices)) {
                Point p1 = figure.getVertex(edge.getVertex1());
                Point p2 = figure.getVertex(edge.getVertex2());
                if (!getHole().containsEdge(p1, p2)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean lengthIsValidFor(int[] vertexIndices) {
        int n = getFigure().getNumEdges();
        for (int i = 0; i < n; i++) {
            Figure.Edge edge = figure.getEdge(i);
            if (shouldCheck(edge, vertexIndices)) {
                long length = figure.getEdgeLengthSquared(i);
                if (length < minLength[i] || length > maxLength[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean shouldCheck(Figure.Edge edge, int[] vertexIndices) {
        return ArrayUtils.contains(vertexIndices, edge.getVertex1()) && ArrayUtils.contains(vertexIndices, edge.getVertex2());
    }

    public boolean isValidEdge(int i) {
        long length = figure.getEdgeLengthSquared(i);
        if (length < minLength[i] || length > maxLength[i]) {
            return false;
        }
        Figure.Edge edge = figure.getEdge(i);
        Point p1 = figure.getVertex(edge.getVertex1());
        Point p2 = figure.getVertex(edge.getVertex2());
        if (!getHole().containsEdge(p1, p2)) {
            return false;
        }
        return true;
    }

    public long dislikes() {
        int n = getHole().getNumVertices();
        int m = getFigure().getNumVertices();
        long sum = 0;
        for (int i = 0; i < n; i++) {
            long minDistance = Long.MAX_VALUE;
            for (int k = 0; k < m; k++) {
                minDistance = Math.min(minDistance, getHole().getVertex(i).distanceSquared(getFigure().getVertex(k)));
            }
            sum += minDistance;
        }
        return sum;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "hole=" + hole.getNumVertices() +
                ", figure=" + figure.getNumVertices() + "," + figure.getNumEdges() +
                ", epsilon=" + epsilon +
                '}';
    }

    private static String baseName(String s) {
        int p = s.lastIndexOf('/');
        if (p < 0) {
            p = s.lastIndexOf('\\');
        }
        if (p >= 0) {
            s = s.substring(p + 1);
        }
        p = s.lastIndexOf('.');
        if (p > 0) {
            return s.substring(0, p);
        } else {
            return s;
        }
    }
}
