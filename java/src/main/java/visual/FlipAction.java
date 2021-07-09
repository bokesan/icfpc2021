package visual;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FlipAction extends AbstractAction {

    private final Gui gui;
    private final boolean horizontal;

    public FlipAction(Gui gui, boolean horizontal) {
        super(horizontal ? "Flip H" : "Flip V");
        this.gui = gui;
        this.horizontal = horizontal;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProblemComponent image = gui.getImage();
        image.flip(horizontal);
        gui.setPose(image.getFigure().getPose());
    }
}
