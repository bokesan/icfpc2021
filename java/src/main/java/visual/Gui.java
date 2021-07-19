package visual;

import model.Force;
import model.Problem;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class Gui {

    private JTextField txtPose;
    private ProblemComponent image;
    private final JLabel lblEpsilon = new JLabel();
    private final JLabel lblNumVertices = new JLabel();
    private final JLabel poseLabel = new JLabel("Pose:");

    public Gui() {
        setup();
    }

    public ProblemComponent getImage() {
        return image;
    }

    private void setup() {
        Dimension sectionSeparator = new Dimension(20, 20);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(1800, 990);
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BoxLayout(statusBar, BoxLayout.X_AXIS));
        statusBar.add(poseLabel);
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
        controls.addSeparator();
        controls.add(new JButton(new Rotate90Action(this, false)));
        controls.add(new JButton(new RotateAction(this, -30)));
        controls.add(new JButton(new RotateAction(this, -5)));
        controls.add(new JButton(new RotateAction(this, 5)));
        controls.add(new JButton(new RotateAction(this, 30)));
        controls.addSeparator(sectionSeparator);
        controls.add(new JLabel("Sel.:"));
        controls.addSeparator();
        controls.add(new JButton(new TranslateAction(this, -1, 0, true)));
        controls.add(new JButton(new TranslateAction(this, 1, 0, true)));
        controls.add(new JButton(new TranslateAction(this, 0, -1, true)));
        controls.add(new JButton(new TranslateAction(this, 0, 1, true)));
        controls.addSeparator();
        controls.add(new JButton(new FlipAction(this, true, true)));
        controls.add(new JButton(new FlipAction(this, false, true)));
        controls.addSeparator();
        controls.add(new JButton(new Rotate90Action(this, true)));
        controls.add(new JButton(new RotateAction(this, -30, true)));
        controls.add(new JButton(new RotateAction(this, -5, true)));
        controls.add(new JButton(new RotateAction(this, 5, true)));
        controls.add(new JButton(new RotateAction(this, 30, true)));
        controls.addSeparator();
        JButton clearSelection = new JButton("Clear");
        controls.add(clearSelection);
        clearSelection.addActionListener(e -> image.clearSelection());

        controls.addSeparator(sectionSeparator);
        controls.add(new JLabel("View"));
        controls.addSeparator();
        JToggleButton edgeLabels = new JToggleButton("Length", true);
        controls.add(edgeLabels);
        edgeLabels.addActionListener(e -> image.toggleEdgeLabels());
        JButton applyForces = new JButton("Forces");
        controls.add(applyForces);
        applyForces.addActionListener(e -> {
            Force force = new Force(image.getProblem(), image.getSelectedVertices());
            force.apply(100);
            image.repaint();
        });

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

    private void setPose(String poseJson) {
        txtPose.setText(poseJson);
    }

    public void updateStatus() {
        poseLabel.setText("Pose (" + image.getProblem().dislikes() + " dislikes):");
        setPose(image.getFigure().getPose().toString());
    }

    public void show(Problem problem) {
        image.setProblem(problem);
        lblEpsilon.setText("Epsilon: " + problem.getEpsilon());
        lblNumVertices.setText("Vertices: " + problem.getFigure().getNumVertices());
    }

}
