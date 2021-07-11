import com.google.common.collect.ImmutableList;
import model.Point;
import model.Polygon;
import model.Problem;
import solvers.Brutus;
import solvers.Parameters;
import solvers.Solver;
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
                        solverParameters.setMaxPositions(Integer.parseInt(args[+i]));
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
                Problem problem = Problem.loadFromFile(files.get(0));
                System.out.println(problem);
                Gui gui = new Gui();
                gui.show(problem);
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
        System.err.println("  run [-gui] file     gui with problem from file");
        System.err.println("  run -solve file..   auto-solve problems from files");
    }

    private static void solve(List<String> files, Parameters parameters) throws IOException {
        List<Function<Parameters, Solver>> solvers = ImmutableList.of(
                ps -> new Brutus(ps)
        );

        combinations(files, solvers).parallel().forEach(entry -> {
            String file = entry.getKey();
            Solver solver = entry.getValue().apply(parameters);
            String solverName = solver.getClass().getSimpleName();
            System.out.format("Solving %s with %s...\n", file, solverName);
            try {
                Problem problem = Problem.loadFromFile(file);
                Point[] pose = solver.solve(problem);
                if (pose == null) {
                    System.out.format("%s %s with %s: no solution.\n", file, problem, solverName);
                } else {
                    System.out.format("%s %s with %s: %s\n", file, problem, solverName, Polygon.toJson(pose));
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
