package visual;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Rotate90Action extends AbstractAction {

    private final Gui gui;

    public Rotate90Action(Gui gui) {
        super("\u2b6e 90°");
        this.gui = gui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProblemComponent image = gui.getImage();
        image.rotateClockwise();
        gui.setPose(image.getFigure().getPose().toString());
    }

}
