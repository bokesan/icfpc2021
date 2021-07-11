package visual;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class TranslateAction extends AbstractAction {

    private final Gui gui;
    private final int x;
    private final int y;

    public TranslateAction(Gui gui, int x, int y) {
        super("Move " + x + "," + y);
        this.gui = gui;
        this.x = x;
        this.y = y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProblemComponent image = gui.getImage();
        image.translateFigure(x, y);
        gui.setPose(image.getFigure().getPose().toString());
    }
}
