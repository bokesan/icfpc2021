package model;

import com.google.common.collect.Range;
import math.Vector2;

import java.util.HashSet;
import java.util.Set;

public class Force {

    private final Problem problem;
    private final Set<Integer> vertices;
    private final Vector2[] forces;
    private final Vector2[] exactVertices;

    public Force(Problem problem, Set<Integer> fixedVertices) {
        this.problem = problem;
        this.vertices = new HashSet<>();
        int n = problem.getFigure().getNumVertices();
        for (int i = 0; i < n; i++) {
            if (!fixedVertices.contains(i)) {
                vertices.add(i);
            }
        }
        exactVertices = new Vector2[n];
        forces = new Vector2[problem.getFigure().getNumVertices()];
    }

    public boolean apply(int maxIterations) {
        copyVerticesFromFigure();
        boolean hasForces = true;
        for (int i = 0; i < maxIterations; i++) {
            if (!computeForces()) {
                hasForces = false;
                break;
            }
            if (!applyForces()) {
                hasForces = false;
                break;
            }
        }
        copyVerticesIntoFigure();
        return hasForces;
    }

    private boolean computeForces() {
        boolean hasForces = false;
        for (int i : vertices) {
            forces[i] = forceOn(i);
            if (!forces[i].isZero()) {
                hasForces = true;
            }
        }
        return hasForces;
    }

    private boolean applyForces() {
        boolean changed = false;
        for (int i : vertices) {
            Vector2 force = forces[i];
            if (!force.isZero()) {
                exactVertices[i] = exactVertices[i].add(force);
                changed = true;
            }
        }
        return changed;
    }

    private Vector2 forceOn(int vertex) {
        Figure figure = problem.getFigure();
        Vector2 p = exactVertices[vertex];
        int nEdges = figure.getNumEdges();
        Vector2 force = Vector2.of(0, 0);
        for (int ei = 0; ei < nEdges; ei++) {
            Figure.Edge edge = figure.getEdge(ei);
            if (edge.hasVertex(vertex)) {
                int vertex2 = edge.getOtherVertex(vertex);
                Vector2 q = exactVertices[vertex2];
                double len = distSquared(p, q);
                long original = figure.getOriginalEdgeLengthSquared(ei);
                if (len > original) {
                    Vector2 v = Vector2.of(q.getX() - p.getX(), q.getY() - p.getY());
                    double factor = (len - original) / (double) original;
                    v = v.scale(Math.min(factor * 0.25, 0.2));
                    force = force.add(v);
                }
                else if (len < original) {
                    Vector2 v = Vector2.of(p.getX() - q.getX(), p.getY() - q.getY());
                    double factor = (original - len) / (double) original;
                    v = v.scale(Math.min(factor * 0.25, 0.2));
                    force = force.add(v);
                }
            }
        }
        return force;
    }

    private void copyVerticesFromFigure() {
        Figure figure = problem.getFigure();
        int n = figure.getNumVertices();
        for (int i = 0; i < n; i++) {
            Point vertex = figure.getVertex(i);
            exactVertices[i] = Vector2.of(vertex.getX(), vertex.getY());
        }
    }

    private void copyVerticesIntoFigure() {
        Figure figure = problem.getFigure();
        int n = figure.getNumVertices();
        for (int i = 0; i < n; i++) {
            Vector2 vertex = exactVertices[i];
            Point p = Point.of(Math.round(vertex.getX()), Math.round(vertex.getY()));
            figure.moveVertex(i, p);
        }
    }

    private static double distSquared(Vector2 a, Vector2 b) {
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        return dx * dx + dy * dy;
    }

}
