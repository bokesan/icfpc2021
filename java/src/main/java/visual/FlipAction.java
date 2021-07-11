package visual;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class FlipAction extends AbstractAction {

    private final Gui gui;
    private final boolean horizontal;
    private final boolean onlySelectedVertices;

    public FlipAction(Gui gui, boolean horizontal) {
        this(gui, horizontal, false);
    }

    public FlipAction(Gui gui, boolean horizontal, boolean onlySelectedVertices) {
        super(horizontal ? "\u2b80" : "\u2b81");
        this.gui = gui;
        this.horizontal = horizontal;
        this.onlySelectedVertices = onlySelectedVertices;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProblemComponent image = gui.getImage();
        image.flip(horizontal, onlySelectedVertices);
        gui.setPose(image.getFigure().getPose().toString());
    }
}
