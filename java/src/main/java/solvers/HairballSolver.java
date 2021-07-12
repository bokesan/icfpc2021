package solvers;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class HairballSolver extends AbstractSolver {

    private Figure figure;

    public HairballSolver(Parameters parameters) {
        super(parameters);
    }

    @Override
    public Pose solve(Problem problem) {
        this.problem = problem;
        this.figure = problem.getFigure();

        if (!problem.isValid()) {
            System.out.println("Error: must start with valid position");
            return null;
        }
        long dislikes = problem.dislikes();
        System.out.println("dislikes: " + dislikes);

        shift(200, dislikes);

        boolean improved = false;
        while (flipTriangle()) {
            improved = true;
            System.out.println("dislikes: " + problem.dislikes());
        }
        if (improved) {
            System.out.println("no further improvements.");
            return figure.getPose();
        } else {
            System.out.println("no improvements found.");
            return null;
        }
    }

    private void shift(int offs, long dislikes) {
        long bestDislikes = dislikes;
        int bestX = 0;
        int bestY = 0;

        for (int x = -offs; x <= offs; x++) {
            for (int y = -offs; y <= offs; y++) {
                if (x != 0 || y != 0) {
                    problem.getFigure().translate(x, y);
                    if (problem.isValid()) {
                        long d = problem.dislikes();
                        if (d < bestDislikes) {
                            bestDislikes = d;
                            bestX = x;
                            bestY = y;
                        }
                    }
                    problem.getFigure().translate(-x, -y);
                }
            }
        }
        if (bestX != 0 || bestY != 0) {
            problem.getFigure().translate(bestX, bestY);
            System.out.format("Best translation: %d,%d, dislikes=%d\n", bestX, bestY, bestDislikes);
        }
    }

    private boolean flipTriangle() {
        int n = problem.getFigure().getNumVertices();
        long dislikes = problem.dislikes();

        for (int vertex = 0; vertex < n; vertex++) {
            Point[] line = getPointy(vertex);
            if (line != null) {
                Point p = figure.getVertex(vertex);
                Point p1 = Geometry.flip(line[0], line[1], p);
                if (!p.equals(p1)) {
                    figure.moveVertex(vertex, p1);
                    long newDislikes = problem.dislikes();
                    if (newDislikes < dislikes && problem.isValid()) {
                        return true;
                    }
                    figure.moveVertex(vertex, p);
                }
            }
        }
        return false;
    }

    private Point[] getPointy(int vertex) {
        List<Figure.Edge> edges = new ArrayList<>();
        for (Figure.Edge edge : figure.getEdges()) {
            if (edge.hasVertex(vertex)) {
                edges.add(edge);
            }
        }
        if (edges.size() != 2)
            return null;
        int v1 = edges.get(0).getOtherVertex(vertex);
        int v2 = edges.get(1).getOtherVertex(vertex);
        Point p1 = figure.getVertex(v1);
        Point p2 = figure.getVertex(v2);
        if (p1.equals(p2))
            return null;
        return new Point[]{p1, p2};
    }

}
