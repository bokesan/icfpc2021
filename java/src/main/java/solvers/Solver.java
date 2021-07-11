package solvers;

import model.Pose;
import model.Problem;

public interface Solver {

    boolean DEBUG_LOG = false;

    Pose solve(Problem problem);

}
