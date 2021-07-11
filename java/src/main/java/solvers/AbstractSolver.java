package solvers;

import model.Problem;

import java.io.*;

public abstract class AbstractSolver implements Solver {

    protected final Parameters parameters;
    protected Problem problem;

    protected AbstractSolver(Parameters parameters) {
        this.parameters = parameters;
    }

    protected void log(String message) {
        String logfileName = problem.getName() + ".log";

        try (FileWriter fw = new FileWriter(logfileName, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw))
        {
            out.println(message);
        } catch (IOException e) {
            throw new RuntimeException("Error logging to file " + logfileName, e);
        }
    }

    protected void logConsole(String message) {
        System.out.format("%s: %s\n", problem.getName(), message);
    }

}
