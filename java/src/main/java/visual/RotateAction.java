package visual;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RotateAction extends AbstractAction {

    private final Gui gui;
    private final int degrees;
    private final boolean onlySelection;

    public RotateAction(Gui gui, int degrees) {
        this(gui, degrees, false);
    }

    public RotateAction(Gui gui, int degrees, boolean onlySelection) {
        super(getLabel(degrees));
        this.gui = gui;
        this.degrees = degrees;
        this.onlySelection = onlySelection;
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
        image.rotate(degrees, onlySelection);
        gui.setPose(image.getFigure().getPose().toString());
    }
}
