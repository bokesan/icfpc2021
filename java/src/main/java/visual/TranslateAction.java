package visual;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TranslateAction extends AbstractAction {

    private final Gui gui;
    private final int x;
    private final int y;
    private final boolean onlySelectedVertices;

    public TranslateAction(Gui gui, int x, int y) {
        super(getLabel(x, y));
        this.gui = gui;
        this.x = x;
        this.y = y;
        this.onlySelectedVertices = false;
    }

    public TranslateAction(Gui gui, int x, int y, boolean onlySelectedVertices) {
        super(getLabel(x, y));
        this.gui = gui;
        this.x = x;
        this.y = y;
        this.onlySelectedVertices = onlySelectedVertices;
    }

    private static String getLabel(int x, int y) {
        if (x == 0) {
            if (y == 1) return "\u2193";
            if (y == 10) return "\u21e9";
            if (y == -1) return "\u2191";
            if (y == -10) return "\u21e7";
        }
        else if (y == 0) {
            if (x == 1) return "\u2192";
            if (x == 10) return "\u21e8";
            if (x == -1) return "\u2190";
            if (x == -10) return "\u21e6";
        }
        return "Move " + x + "," + y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProblemComponent image = gui.getImage();
        image.translateFigure(x, y, onlySelectedVertices);
        gui.setPose(image.getFigure().getPose().toString());
    }
}
