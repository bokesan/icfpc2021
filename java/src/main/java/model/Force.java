package model;

import com.google.common.collect.Range;
import math.Vector2;

import java.util.HashSet;
import java.util.Set;

public class Force {

    private final Problem problem;
    private final Set<Integer> vertices;
    private final Vector2[] forces;

    public Force(Problem problem, Set<Integer> fixedVertices) {
        this.problem = problem;
        this.vertices = new HashSet<>();
        int n = problem.getFigure().getNumVertices();
        for (int i = 0; i < n; i++) {
            if (!fixedVertices.contains(i)) {
                vertices.add(i);
            }
        }
        forces = new Vector2[problem.getFigure().getNumVertices()];
    }

    public boolean apply(int maxIterations) {
        for (int i = 0; i < maxIterations; i++) {
            if (!computeForces()) {
                return false;
            }
            if (!applyForces()) {
                return false;
            }
        }
        return true;
    }

    private boolean computeForces() {
        boolean hasForces = false;
        for (int i : vertices) {
            forces[i] = on(problem, i);
            if (!forces[i].isZero()) {
                hasForces = true;
            }
        }
        return hasForces;
    }

    private boolean applyForces() {
        boolean changed = false;
        for (int i : vertices) {
            long dx = Math.round(forces[i].getX());
            long dy = Math.round(forces[i].getY());
            if (dx != 0 || dy != 0) {
                problem.getFigure().moveVertex(i, problem.getFigure().getVertex(i).translate(dx, dy));
                changed = true;
            }
        }
        return changed;
    }

    public Vector2 on(Problem problem, int vertex) {
        Figure figure = problem.getFigure();
        Point p = figure.getVertex(vertex);
        int nEdges = figure.getNumEdges();
        Vector2 force = Vector2.of(0, 0);
        for (int ei = 0; ei < nEdges; ei++) {
            Figure.Edge edge = figure.getEdge(ei);
            if (edge.hasVertex(vertex)) {
                long len = figure.getEdgeLengthSquared(ei);
                long original = figure.getOriginalEdgeLengthSquared(ei);
                if (len > original) {
                    Point q = figure.getVertex(edge.getOtherVertex(vertex));
                    Vector2 v = Vector2.of(q.getX() - p.getX(), q.getY() - p.getY());
                    double factor = (len - original) / (double) original;
                    v = v.scale(Math.min(factor * 0.25, 0.2));
                    force = force.add(v);
                }
                else if (len < original) {
                    Point q = figure.getVertex(edge.getOtherVertex(vertex));
                    Vector2 v = Vector2.of(p.getX() - q.getX(), p.getY() - q.getY());
                    double factor = (original - len) / (double) original;
                    v = v.scale(Math.min(factor * 0.25, 0.2));
                    force = force.add(v);
                }
            }
        }
        return force;
    }

}
