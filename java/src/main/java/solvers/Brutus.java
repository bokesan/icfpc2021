package solvers;

import model.Bounds;
import model.Point;
import model.Polygon;
import model.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Brutus extends AbstractSolver {

    private static final int RESEED_INTERVAL = 200000;

    private int reseedCounter;

    private int numVertices;
    private boolean exhaustive;

    private Point[] bestSolution;
    private long bestDislikes;

    private final List<Point> pointsInsideHole = new ArrayList<>(2000);

    public Brutus(Parameters parameters) {
        super(parameters);
    }

    @Override
    public Point[] solve(Problem problem) {
        this.problem = problem;
        Bounds bounds = problem.getBounds();
        this.numVertices = problem.getFigure().getNumVertices();

        pointsInsideHole.clear();
        for (long x = bounds.getMinX(); x <= bounds.getMaxX(); x++) {
            for (long y = bounds.getMinY(); y <= bounds.getMaxY(); y++) {
                Point p = Point.of(x, y);
                if (problem.getHole().contains(p)) {
                    pointsInsideHole.add(p);
                }
            }
        }

        log(problem.getName());
        log(String.format("Vertices: %d. Candidates: %d\n", numVertices, pointsInsideHole.size()));

        bestDislikes = Long.MAX_VALUE;
        bestSolution = null;
        exhaustive = false;

        int alt = 0;
        while (bestDislikes > 0 && !exhaustive) {
            if ((alt & 1) == 0)
                moveHoleVerticesToFront(pointsInsideHole);
            alt++;
            reseedCounter = RESEED_INTERVAL;
            fill(0);
            /*
            if (bestSolution == null) {
                System.out.println("Reseeding...");
            } else {
                System.out.format("Reseeding... Best so far (%d dislikes): %s\n", bestDislikes, Polygon.toPose(bestSolution));
            }
             */
            Collections.shuffle(pointsInsideHole);
        }
        if (bestSolution != null) {
            log(String.format("Best solution (%d dislikes): %s\n", bestDislikes, Polygon.toPose(bestSolution)));
        }
        log("Done.");

        return bestSolution;
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

    private void fill(int i) {
        reseedCounter--;
        if (i == numVertices) {
            // we have a solution?!
            if (problem.isValid()) {
                long dislikes = problem.dislikes();
                if (dislikes < bestDislikes) {
                    bestDislikes = dislikes;
                    bestSolution = Arrays.copyOf(problem.getFigure().getVertices(), problem.getFigure().getNumVertices());
                    log(String.format("Solution with %d dislikes: %s\n", dislikes, problem.getFigure().getPose()));
                    logConsole(String.format("new solution with %d dislikes found.", dislikes));
                    reseedCounter = RESEED_INTERVAL;
                }
            }
            return;
        }

        // examine just a random subset of points for large holes
        int n = Math.min(pointsInsideHole.size(), Math.max(parameters.getMaxPositions(), problem.getFigure().getNumVertices()));
        for (int k = 0; k < n; k++) {
            Point p = pointsInsideHole.get(k);
            problem.getFigure().moveVertex(i, p);
            if (problem.isValidUpTo(i)) {
                fill(i + 1);
                if (bestDislikes == 0 || reseedCounter <= 0) {
                    return;
                }
            }
            k++;
        }
        if (i == 0) {
            exhaustive = true;
        }
    }
}
