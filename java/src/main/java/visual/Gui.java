package visual;

import model.Problem;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Gui {

    private JFrame frame;

    private JTextField txtPose;
    private ProblemComponent image;
    private final JLabel lblEpsilon = new JLabel();
    private final JLabel lblNumVertices = new JLabel();

    public Gui() {
        setup();
    }

    public ProblemComponent getImage() {
        return image;
    }

    public void setPose(String poseJson) {
        txtPose.setText(poseJson);
    }

    private void setup() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(1800, 990);
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusBar.add(new JLabel("Pose:"));
        txtPose = new JTextField(100);
        statusBar.add(txtPose);
        JButton copyToClipboardButton = new JButton("Copy");
        statusBar.add(copyToClipboardButton);
        copyToClipboardButton.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(txtPose.getText());
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });
        image = new ProblemComponent(this);
        Dimension imageSize = new Dimension(1700, 900);
        image.setSize(imageSize);
        image.setMinimumSize(imageSize);
        image.setPreferredSize(imageSize);
        frame.add(statusBar, BorderLayout.SOUTH);
        frame.add(image, BorderLayout.CENTER);
        JToolBar controls = new JToolBar(null, JToolBar.VERTICAL);
        // controls.setLayout(new BoxLayout(controls, BoxLayout.PAGE_AXIS));
        controls.add(new JButton(new TranslateAction(this, -1, 0)));
        controls.add(new JButton(new TranslateAction(this, 1, 0)));
        controls.add(new JButton(new TranslateAction(this, 0, -1)));
        controls.add(new JButton(new TranslateAction(this, 0, 1)));
        controls.addSeparator();
        controls.add(new JButton(new TranslateAction(this, -10, 0)));
        controls.add(new JButton(new TranslateAction(this, 10, 0)));
        controls.add(new JButton(new TranslateAction(this, 0, -10)));
        controls.add(new JButton(new TranslateAction(this, 0, 10)));
        controls.addSeparator();
        controls.add(new JButton(new FlipAction(this, true)));
        controls.add(new JButton(new FlipAction(this, false)));
        controls.add(new JButton(new Rotate90Action(this)));
        controls.add(new JButton(new RotateAction(this, -30)));
        controls.add(new JButton(new RotateAction(this, -5)));
        controls.add(new JButton(new RotateAction(this, 5)));
        controls.add(new JButton(new RotateAction(this, 30)));
        controls.addSeparator(new Dimension(20,20));
        controls.add(new JLabel("Selection"));
        controls.add(new JButton(new TranslateAction(this, -1, 0, true)));
        controls.add(new JButton(new TranslateAction(this, 1, 0, true)));
        controls.add(new JButton(new TranslateAction(this, 0, -1, true)));
        controls.add(new JButton(new TranslateAction(this, 0, 1, true)));
        controls.addSeparator();
        controls.add(new JButton(new FlipAction(this, true, true)));
        controls.add(new JButton(new FlipAction(this, false, true)));
        controls.addSeparator();
        JButton clearSelection = new JButton("Clear");
        controls.add(clearSelection);
        clearSelection.addActionListener(e -> image.clearSelection());
        frame.add(controls, BorderLayout.EAST);
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.PAGE_AXIS));
        info.add(lblEpsilon);
        info.add(lblNumVertices);
        frame.add(info, BorderLayout.WEST);
        frame.setLocationRelativeTo(null);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
    }

    public void show(Problem problem) {
        image.setProblem(problem);
        lblEpsilon.setText("Epsilon: " + problem.getEpsilon());
        lblNumVertices.setText("Vertices: " + problem.getFigure().getNumVertices());
    }

}
