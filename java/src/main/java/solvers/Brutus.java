package solvers;

import model.Bounds;
import model.Point;
import model.Polygon;
import model.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Brutus implements Solver {

    private Problem problem;
    private Bounds bounds;
    private int numVertices;

    private Point[] bestSolution;
    private long bestDislikes;

    private List<Point> pointsInsideHole = new ArrayList<>(10000);


    @Override
    public Point[] solve(Problem problem) {
        this.problem = problem;
        this.bounds = problem.getBounds();
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

        bestDislikes = Long.MAX_VALUE;
        bestSolution = null;

        fill(0);

        if (bestSolution != null) {
            System.out.format("Best solution (%d dislikes): %s\n", bestDislikes, Polygon.toJson(bestSolution));
        }

        return bestSolution;
    }

    private void fill(int i) {
        if (i == numVertices) {
            // we have a solution?!
            if (problem.isValid()) {
                long dislikes = problem.dislikes();
                if (dislikes < bestDislikes) {
                    bestDislikes = dislikes;
                    bestSolution = Arrays.copyOf(problem.getFigure().getVertices(), problem.getFigure().getNumVertices());
                    System.out.format("Solution with %d dislikes: %s\n", dislikes, problem.getFigure().getPose());
                }
            }
            return;
        }

        int n = pointsInsideHole.size();
        int k = 0;
        for (Point p : pointsInsideHole) {
            if (i == 0) {
                System.out.format("Progress: %d%%\n", 100 * k / n);
            }
            problem.getFigure().moveVertex(i, p);
            if (problem.isValidUpTo(i)) {
                fill(i + 1);
            }
            k++;
        }
    }
}
