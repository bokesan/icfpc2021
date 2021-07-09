package solvers;

import model.Point;
import model.Problem;

public interface Solver {

    boolean DEBUG_LOG = false;

    Point[] solve(Problem problem);

}
