import com.google.common.collect.ImmutableList;
import model.Pose;
import model.Problem;
import solvers.*;
import visual.Gui;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

public class Launcher {

    private enum Command {
        GUI,
        SOLVE
    }

    public static void main(String... args) throws IOException {
        Command command = Command.GUI;
        List<String> files = new ArrayList<>();
        Parameters solverParameters = new Parameters();

        if (args.length == 0) {
            usage();
            return;
        }
        int i = 0;
        while (i < args.length) {
            if (args[i].startsWith("-")) {
                switch (args[i]) {
                    case "-gui":
                        command = Command.GUI;
                        break;
                    case "-solve":
                        command = Command.SOLVE;
                        break;
                    case "-max-positions":
                        solverParameters.setMaxPositions(Integer.parseInt(args[++i]));
                        break;
                    case "-translate-x":
                        solverParameters.setTranslationX(Integer.parseInt(args[++i]));
                        break;
                    case "-translate-y":
                        solverParameters.setTranslationY(Integer.parseInt(args[++i]));
                        break;
                    default:
                        System.err.println("Unknown option: " + args[i]);
                        usage();
                        return;
                }
            } else {
                files.add(args[i]);
            }
            i++;
        }

        switch (command) {
            case GUI:
                if (files.isEmpty()) {
                    usage();
                    return;
                }
                showGui(files, solverParameters);
                break;
            case SOLVE:
                solve(files, solverParameters);
                break;
            default:
                throw new AssertionError();
        }
    }

    private static void usage() {
        System.err.println("usage:");
        System.err.println("  run problem [pose]    gui with problem from file");
        System.err.println("  run -solve file..     auto-solve problems from files");
    }

    private static void showGui(List<String> files, Parameters parameters) throws IOException {
        Problem problem = Problem.loadFromFile(files.get(0));
        problem.getFigure().translate(parameters.getTranslationX(), parameters.getTranslationY());
        System.out.println(problem);
        if (files.size() > 1) {
            Pose pose = Pose.loadFromFile(files.get(1));
            problem.getFigure().setPose(pose);
        }
        Gui gui = new Gui();
        gui.show(problem);
    }

    private static void solve(List<String> files, Parameters parameters) {
        List<Function<Parameters, Solver>> solvers = ImmutableList.of(
                Brutus::new,
                Jan::new,
                ExactMatchBruteforceSolver::new
                // HairballSolver::new
        );

        combinations(files, solvers).parallel().forEach(entry -> {
            String file = entry.getKey();
            Solver solver = entry.getValue().apply(parameters);
            String solverName = solver.getClass().getSimpleName();
            System.out.format("Solving %s with %s...\n", file, solverName);
            try {
                Problem problem = Problem.loadFromFile(file);
                problem.getFigure().translate(parameters.getTranslationX(), parameters.getTranslationY());
                Pose pose = solver.solve(problem);
                if (pose == null) {
                    System.out.format("%s with %s: no solution.\n", file, solverName);
                } else {
                    System.out.format("%s with %s: %s\n", file, solverName, pose);
                }
            } catch (IOException e) {
                System.err.println("Error loading " + file + ": " + e);
            }
        });

    }

    private static Stream<Map.Entry<String, Function<Parameters, Solver>>> combinations(List<String> strings, List<Function<Parameters, Solver>> solvers) {
        Stream.Builder<Map.Entry<String, Function<Parameters, Solver>>> builder = Stream.builder();
        for (String s : strings) {
            for (Function<Parameters, Solver> creator : solvers) {
                builder.add(new AbstractMap.SimpleImmutableEntry<>(s, creator));
            }
        }
        return builder.build();
    }

}
