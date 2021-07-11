package solvers;

import com.google.common.collect.Collections2;
import model.Figure;
import model.Point;
import model.Problem;

import java.util.ArrayList;
import java.util.List;

public class ExactMatchBruteforceSolver implements Solver {

    @Override
    public Point[] solve(Problem problem) {
        Figure figure = problem.getFigure();
        int n = problem.getHole().getNumVertices();
        int m = figure.getNumVertices();
        if (n > m || m >= 12) {
            return null;
        }
        List<Integer> indices = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            indices.add(i);
        }

        int edges = figure.getNumEdges();
        for (List<Integer> ixs : Collections2.permutations(indices)) {
            for (int i = 0; i < n; i++) {
                figure.moveVertex(ixs.get(i), problem.getHole().getVertex(i));
            }
            boolean ok = true;
            for (int i = 0; i < edges; i++) {
                if (figure.getEdgeLengthSquared(i) != figure.getOriginalEdgeLengthSquared(i)) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                if (!problem.isValid()) {
                    System.err.println("INCORRECT!");
                }
                return figure.getVertices();
            }
        }
        return null;
    }
}
