package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Problem {

    private final Polygon hole;
    private final Figure figure;
    private final long epsilon;
    private final double epsilonMil;

    public Problem(Polygon hole, Figure figure, long epsilon) {
        this.hole = hole;
        this.figure = figure;
        this.epsilon = epsilon;
        this.epsilonMil = (double) epsilon / 1000000;
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
        return of(json);
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

    public boolean isValidUpTo(int vertexIndex) {
        int n = getFigure().getNumEdges();
        for (int i = 0; i < n; i++) {
            Figure.Edge edge = figure.getEdge(i);
            if (edge.getVertex1() <= vertexIndex && edge.getVertex2() <= vertexIndex) {
                Point p1 = figure.getEdgeStart(i);
                Point p2 = figure.getEdgeEnd(i);
                if (!getHole().containsEdge(p1, p2)) {
                    return false;
                }
                long length = figure.getEdgeLengthSquared(i);
                long orig = figure.getOriginalEdgeLengthSquared(i);
                if (Math.abs((double) length / orig - 1) > epsilonMil) {
                    return false;
                }
            }
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
}
