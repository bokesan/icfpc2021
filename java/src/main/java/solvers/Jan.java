package solvers;

import model.*;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Jan extends AbstractSolver {

    private Polygon hole;
    private Figure figure;

    private final List<Point> pointsInsideHole = new ArrayList<>(2000);

    private Pose bestSolution;
    private long leastDislikes;

    public Jan(Parameters parameters) {
        super(parameters);
    }

    @Override
    public Pose solve(Problem problem) {
        this.problem = problem;
        hole = problem.getHole();
        figure = problem.getFigure();
        Bounds bounds = problem.getBounds();
        pointsInsideHole.clear();
        for (long x = bounds.getMinX(); x <= bounds.getMaxX(); x++) {
            for (long y = bounds.getMinY(); y <= bounds.getMaxY(); y++) {
                Point p = Point.of(x, y);
                if (problem.getHole().contains(p)) {
                    pointsInsideHole.add(p);
                }
            }
        }
        moveHoleVerticesToFront(pointsInsideHole);

        bestSolution = null;
        leastDislikes = Long.MAX_VALUE;

        // Check figure edges for matching hole edge
        for (int ei = 0; ei < figure.getNumEdges(); ei++) {
            long minLen = problem.getMinLength(ei);
            long maxLen = problem.getMaxLength(ei);
            for (int hi = 0; hi < hole.getNumVertices(); hi++) {
                long hLen = hole.getEdgeLengthSquared(hi);
                if (minLen <= hLen && hLen <= maxLen) {
                    Figure.Edge edge = figure.getEdge(ei);
                    // we can place the edge here, in either orientation
                    if (startSearch(hi, edge.getVertex1(), edge.getVertex2())) {
                        return figure.getPose();
                    }
                    if (startSearch(hi, edge.getVertex2(), edge.getVertex1())) {
                        return figure.getPose();
                    }
                }
            }
        }
        return bestSolution;
    }

    private boolean startSearch(int hi, int v1, int v2) {
        BitSet knownVertices = new BitSet();
        figure.moveVertex(v1, hole.getVertex(hi));
        figure.moveVertex(v2, hole.getVertex((hi == hole.getNumVertices() - 1) ? 0 : (hi+1)));
        knownVertices.set(v1);
        knownVertices.set(v2);
        return search(knownVertices);
    }

    private boolean search(BitSet knownVertices) {
        if (allVerticesKnown(knownVertices)) {
            long dislikes = problem.dislikes();
            if (dislikes < leastDislikes) {
                leastDislikes = dislikes;
                bestSolution = figure.getPose();
                log(String.format("Solution with %d dislikes: %s\n", dislikes, bestSolution));
                logConsole(String.format("new solution with %d dislikes found.", dislikes));
            }
            return dislikes == 0;
        }
        int nv = figure.getNumVertices();
        for (int v = 0; v < nv; v++) {
            if (!knownVertices.get(v) && hasEdge(v, knownVertices)) {
                knownVertices.set(v);
                for (Point p : pointsInsideHole) {
                    figure.moveVertex(v, p);
                    if (problem.isValidFor(knownVertices)) {
                        if (search(knownVertices)) {
                            return true;
                        }
                    }
                }
                knownVertices.clear(v);
            }
        }
        return false;
    }

    private boolean allVerticesKnown(BitSet knownVertices) {
        return knownVertices.cardinality() == figure.getNumVertices();
    }

    /**
     * Check that the vertex has at least one edge to a vertex in the known vertices collection.
     */
    private boolean hasEdge(int vertex, BitSet knownVertices) {
        for (Figure.Edge edge : figure.getEdges()) {
            if (edge.hasVertex(vertex) && knownVertices.get(edge.getOtherVertex(vertex))) {
                return true;
            }
        }
        return false;
    }

    private void moveHoleVerticesToFront(List<Point> pointsInsideHole) {
        int n = pointsInsideHole.size() - 1;
        int front = 0;
        while (front < n) {
            Point p = pointsInsideHole.get(n);
            if (problem.getHole().containsVertex(p)) {
                // swap
                pointsInsideHole.set(n, pointsInsideHole.get(front));
                pointsInsideHole.set(front, p);
                front++;
            }
            n--;
        }
    }

}
