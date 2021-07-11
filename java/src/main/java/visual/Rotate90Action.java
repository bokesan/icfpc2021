package visual;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class Rotate90Action extends AbstractAction {

    private final Gui gui;
    private final boolean onlySelection;

    public Rotate90Action(Gui gui, boolean onlySelection) {
        super("\u2b6e 90°");
        this.gui = gui;
        this.onlySelection = onlySelection;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProblemComponent image = gui.getImage();
        image.rotateClockwise(onlySelection);
        gui.updateStatus();
    }

}
