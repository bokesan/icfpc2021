package visual;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RotateAction extends AbstractAction {

    private final Gui gui;
    private final int degrees;

    public RotateAction(Gui gui, int degrees) {
        super("Rot " + ((degrees < 0) ? (-degrees + "° CCW") : (degrees + "° CW")));
        this.gui = gui;
        this.degrees = degrees;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProblemComponent image = gui.getImage();
        image.rotate(degrees);
        gui.setPose(image.getFigure().getPose());
    }
}
