package visual;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RotateAction extends AbstractAction {

    private final Gui gui;
    private final int degrees;

    public RotateAction(Gui gui, int degrees) {
        super(getLabel(degrees));
        this.gui = gui;
        this.degrees = degrees;
    }

    private static String getLabel(int a) {
        if (a < 0) {
            return "\u2b6f " + (-a) + "°";
        } else {
            return "\u2b6e " + a + "°";
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProblemComponent image = gui.getImage();
        image.rotate(degrees);
        gui.setPose(image.getFigure().getPose().toString());
    }
}
