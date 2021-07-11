package visual;

import model.Bounds;
import model.Figure;
import model.Point;
import model.Problem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.HashSet;
import java.util.Set;

public class ProblemComponent extends JComponent {

    private final Gui gui;
    private Problem problem;
    private final Set<Integer> selectedVertices = new HashSet<>();

    public ProblemComponent(Gui gui) {
        this.gui = gui;
        this.addMouseListener(new MyMouseListener());
    }

    public Problem getProblem() {
        return problem;
    }

    public Gui getGui() {
        return gui;
    }

    public Figure getFigure() {
        return getProblem().getFigure();
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
        this.problemBounds = problem.getBounds();
        this.selectedVertices.clear();
        repaint();
    }

    public void translateFigure(int x, int y, boolean onlySelectedVertices) {
        if (onlySelectedVertices) {
            for (int vertex : selectedVertices) {
                getFigure().translate(vertex, x, y);
            }
        } else {
            getFigure().translate(x, y);
        }
        repaint();
    }

    public void flip(boolean horizontal) {
        if (horizontal) {
            getFigure().flipHorizontal();
        } else {
            getFigure().flipVertical();
        }
        repaint();
    }

    public void rotateClockwise() {
        problem.getFigure().rotate90CW();
        repaint();
    }

    public void rotate(int degrees) {
        getFigure().rotate(degrees);
        repaint();
    }

    public void moveVertex(int vertexIndex, Point newPosition) {
        getFigure().moveVertex(vertexIndex, newPosition);
        repaint();
    }

    private static final int BORDER = 50;

    private int gWidth;
    private int gHeight;
    private Bounds problemBounds;

    private float scaleFactor() {
        float wf = (gWidth - 2*BORDER) / (float) problemBounds.getWidth();
        float hf = (gHeight - 2*BORDER) / (float) problemBounds.getHeight();
        return Math.min(wf, hf);
    }

    private float translateX(Graphics2D g, long x) {
        return BORDER + (x - problemBounds.getMinX()) * scaleFactor();
    }

    private float translateY(Graphics2D g, long y) {
        int h = gHeight - 2*BORDER;
        return BORDER + (y - problemBounds.getMinY()) * scaleFactor();
    }

    protected long reverseTranslateX(long sx) {
        float scale = scaleFactor();
        return Math.round(sx / scale + problemBounds.getMinX() - BORDER / scale);
    }

    protected long reverseTranslateY(long sy) {
        float scale = scaleFactor();
        return Math.round(sy / scale + problemBounds.getMinY() - BORDER / scale);
    }

    private void toggleSelected(int vertex) {
        if (!selectedVertices.add(vertex)) {
            selectedVertices.remove(vertex);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        gWidth = this.getWidth();
        gHeight = this.getHeight();

        super.paintComponent(g);
        g.setColor(Color.BLACK);
        showPolygon((Graphics2D) g, problem.getHole().getVertices());
        g.setColor(Color.RED);
        showFigure((Graphics2D) g, problem.getFigure());
        g.setColor(Color.BLACK);
    }

    private void showPolygon(Graphics2D g, model.Point[] points) {
        int n = points.length;
        for (int i = 1; i < n; i++) {
            showLine(g, points[i-1], points[i]);
            labelPoint(g, points[i], i, false);
        }
        showLine(g, points[n-1], points[0]);
        labelPoint(g, points[0], 0, false);
    }

    private void labelPoint(Graphics2D g, Point point, int i, boolean isFigure) {
        float x = translateX(g, point.getX());
        float y = translateY(g, point.getY());
        g.drawString("V" + i, x + 5, y + 2);
        int size;
        if (isFigure && selectedVertices.contains(i)) {
            size = 7;
        } else {
            size = 3;
        }
        int h = size / 2;
        Ellipse2D.Double circle = new Ellipse2D.Double(x-h, y-h, size, size);
        g.fill(circle);
    }

    private void showFigure(Graphics2D g, Figure figure) {
        int n = figure.getNumEdges();
        for (int i = 0; i < n; i++) {
            Point p1 = figure.getEdgeStart(i);
            Point p2 = figure.getEdgeEnd(i);
            long length = figure.getEdgeLengthSquared(i);
            long orig = figure.getOriginalEdgeLengthSquared(i);
            String state;
            if (Math.abs((double) length / orig - 1) <= (double) problem.getEpsilon() / 1000000) {
                state = "";
            } else {
                state = " (" + orig + ")";
            }
            String label = length + state;
            if (!state.isEmpty())
                g.setColor(Color.BLUE);
            showLine(g, p1, p2, label);
            g.setColor(Color.RED);
        }
        int m = figure.getNumVertices();
        for (int i = 0; i < m; i++) {
            labelPoint(g, figure.getVertex(i), i, true);
        }
    }

    private void showLine(Graphics2D g, Point p1, Point p2) {
        showLine(g, p1, p2, Long.toString(p1.distanceSquared(p2)));
    }

    private void showLine(Graphics2D g, Point p1, Point p2, String label) {
        float x1 = translateX(g, p1.getX());
        float y1 = translateY(g, p1.getY());
        float x2 = translateX(g, p2.getX());
        float y2 = translateY(g, p2.getY());
        g.draw(new Line2D.Float(x1,y1,x2,y2));
        float cx = (x1 + x2) / 2;
        float cy = (y1 + y2) / 2;
        if (label != null) {
            g.drawString(label, cx + 5, cy + 5);
        }
    }

    private class MyMouseListener extends MouseAdapter {

        private int selectedVertex = -1;

        @Override
        public void mousePressed(MouseEvent e) {
            long x = reverseTranslateX(e.getX());
            long y = reverseTranslateY(e.getY());
            selectedVertex = getFigure().findVertex(x, y);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (selectedVertex >= 0) {
                if (e.isControlDown()) {
                    toggleSelected(selectedVertex);
                    repaint();
                } else {
                    long x = reverseTranslateX(e.getX());
                    long y = reverseTranslateY(e.getY());
                    moveVertex(selectedVertex, Point.of(x, y));
                    getGui().setPose(getFigure().getPose().toString());
                }
            }
        }
    }

}
