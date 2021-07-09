import model.Problem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import visual.Gui;

import java.io.IOException;

public class Launcher {

    private static final String INPUT_DIR = "data/input";
    private static final String OUTPUT_DIR = "data/output";
    private static final Logger _logger = LoggerFactory.getLogger(Launcher.class);

    public static void main(String... args) throws IOException {
        String problemFile = args[0];
        Problem problem = Problem.loadFromFile(problemFile);
        System.out.println(problem);
        Gui gui = new Gui();
        gui.show(problem);
    }

}
