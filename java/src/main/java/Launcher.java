import model.Point;
import model.Polygon;
import model.Problem;
import solvers.ExactMatchBruteforceSolver;
import solvers.Solver;
import visual.Gui;

import java.io.IOException;
import java.util.Arrays;

public class Launcher {

    public static void main(String... args) throws IOException {
        switch (args.length) {
            case 0:
                usage();
                break;
            case 1:
                String problemFile = args[0];
                Problem problem = Problem.loadFromFile(problemFile);
                System.out.println(problem);
                Gui gui = new Gui();
                gui.show(problem);
                break;
            default:
                switch (args[0]) {
                    case "-solve":
                        solve(Arrays.copyOfRange(args, 1, args.length), new ExactMatchBruteforceSolver());
                        break;
                    default:
                        usage();
                        break;
                }
                break;
        }
    }

    private static void usage() {
        System.out.println("usage:");
        System.out.println("  run file            gui with problem from file");
        System.out.println("  run -solve file..   auto-solve problems from files");
    }

    private static void solve(String[] files, Solver... solvers) throws IOException {
        for (String file : files) {
            for (Solver solver : solvers) {
                Problem problem = Problem.loadFromFile(file);
                Point[] pose = solver.solve(problem);
                String solverName = solver.getClass().getSimpleName();
                if (pose == null) {
                    System.out.format("%s %s with %s: nope.\n", file, problem, solverName);
                } else {
                    System.out.format("%s %s with %s: %s\n", file, problem, solverName, Polygon.toJson(problem.getFigure().getVertices()));
                }
            }
        }
    }

}
