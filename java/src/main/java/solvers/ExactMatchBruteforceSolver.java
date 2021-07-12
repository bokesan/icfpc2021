package solvers;

import model.*;

import java.util.*;

/**
 * Assuming there exists a perfect solution (0 dislikes),
 * Brute-force distribute the hole vertices among the figure vertices so that the edge constraints
 * are satisfied only for those vertices.
 * Then solve the remaining vertices (if any).
 */
public class ExactMatchBruteforceSolver extends AbstractSolver {

    private static final boolean DEBUG = false;

    private Figure figure;

    public ExactMatchBruteforceSolver(Parameters parameters) {
        super(parameters);
    }

    @Override
    public Pose solve(Problem problem) {
        this.problem = problem;
        this.figure = problem.getFigure();
        if (DEBUG) log("Solving " + problem);
        int n = problem.getHole().getNumVertices();
        int m = figure.getNumVertices();
        if (n > m) {
            log("Solver not applicable because figure has fewer vertices than hole");
            return null;
        }

        return tryBorder(0, new BitSet(), new boolean[m]);
    }

    /**
     * Try placing hole vertices.
     * @param holeIndex the next hole vertex to place
     */
    private Pose tryBorder(int holeIndex, BitSet borderVertices, boolean[] used) {
        Polygon hole = problem.getHole();
        if (holeIndex == hole.getNumVertices()) {
            return tryFit(borderVertices);
        }
        Point point = hole.getVertex(holeIndex);
        int n = figure.getNumVertices();
        for (int i = 0; i < n; i++) {
            if (!used[i]) {
                figure.moveVertex(i, point);
                borderVertices.set(i);
                used[i] = true;
                Pose solution = tryBorder(holeIndex + 1, borderVertices, used);
                if (solution != null) {
                    return solution;
                }
                used[i] = false;
            }
        }
        return null;
    }

    private Pose tryFit(BitSet borderVertices) {
        if (!problem.isValidFor(borderVertices)) {
            if (DEBUG) {
                if (!problem.lengthIsValidFor(borderVertices)) {
                    log("Vertex length not valid for " + borderVertices);
                }
                if (!problem.insideIsValidFor(borderVertices)) {
                    log("Inside check failed for " + borderVertices);
                }
            }
            return null;
        }
        // count edges between border vertices
        int numEdges = 0;
        for (Figure.Edge edge : figure.getEdges()) {
            if (borderVertices.get(edge.getVertex1()) && borderVertices.get(edge.getVertex2())) {
                numEdges++;
            }
        }
        if (numEdges != problem.getHole().getNumVertices()) {
            if (DEBUG) {
                log("Probably not a border. " + problem.getHole().getNumVertices() + " vertices, but " + numEdges + " edges: "
                        + borderVertices);
            }
            if (numEdges <= 1)
                return null;
        }
        log("Border found: " + borderVertices + ". Placing remaining vertices.");

        int n = figure.getNumVertices();
        if (problem.getHole().getNumVertices() == n) {
            Pose pose = figure.getPose();
            log("Found solution. " + pose);
            return pose;
        }
        // place other vertices using their constraints
        Set<Integer> placed = new HashSet<>();
        borderVertices.stream().forEach(placed::add);

        if (DEBUG) {
            log("Vertices left to place: " + (n - placed.size()));
        }
        boolean changed = true;
        while (changed && placed.size() < n) {
            changed = false;
            for (int i = 0; i < n; i++) {
                if (!placed.contains(i)) {
                    if (tryToPlace(i, placed)) {
                        placed.add(i);
                        changed = true;
                    }
                }
            }
        }

        if (placed.size() == n) {
            return figure.getPose();
        }

        log("no placement possible");
        return null;
    }

    private boolean tryToPlace(int vertex, Set<Integer> placed) {
        List<Integer> edges = new ArrayList<>();
        int numEdges = figure.getNumEdges();
        boolean allValid = true;
        int edgesToPlaced = 0;
        for (int i = 0; i < numEdges; i++) {
            Figure.Edge edge = figure.getEdge(i);
            if (edge.hasVertex(vertex)) {
                edges.add(i);
                if (placed.contains(edge.getOtherVertex(vertex))) {
                    edgesToPlaced++;
                }
                allValid = allValid && problem.isValidEdge(i);
            }
        }

        if (DEBUG) {
            StringBuilder b = new StringBuilder();
            b.append("Trying to place vertex ");
            b.append(vertex);
            b.append(". with edges to ");
            String sep = "";
            for (int edge : edges) {
                Figure.Edge edge1 = figure.getEdge(edge);
                int dest = edge1.getOtherVertex(vertex);
                b.append(sep);
                b.append(dest);
                b.append(':');
                b.append(placed.contains(dest) ? figure.getVertex(dest) : "[?,?]");
                b.append('(');
                b.append(String.format("%.1f", Math.sqrt(figure.getOriginalEdgeLengthSquared(edge))));
                b.append(')');
                sep = ", ";
            }
            b.append('.');
            log(b.toString());
        }

        if (edgesToPlaced < 3 && edgesToPlaced < edges.size()) {
            // not yet ready for placement
            if (DEBUG) {
                log("Not yet ready for placement");
            }
            return false;
        }

        if (allValid && !edges.isEmpty()) {
            if (DEBUG) log(" all valid and edges do exist");
            return true;
        }

        Point current = figure.getVertex(vertex);

        switch (edges.size()) {
            case 0:
                log("Unconnected vertex found!");
                if (problem.getHole().contains(current)) {
                    return true;
                }
                // Just take any placed
                int extant = placed.stream().findAny().get();
                figure.moveVertex(vertex, figure.getVertex(extant));
                return true;
            case 1:
                // TODO: place
                log("HAIRY 100! Just one edge to " + figure.getVertex(figure.getEdge(edges.get(0)).getOtherVertex(vertex)));
                return false;
            case 2:
                int edge1 = edges.get(0);
                int edge2 = edges.get(1);
                assert edge1 != edge2;
                Point p1 = figure.getVertex(figure.getEdge(edge1).getOtherVertex(vertex));
                Point p2 = figure.getVertex(figure.getEdge(edge2).getOtherVertex(vertex));
                if (p1.equals(p2)) {
                    log("HAIRY 200 [vertex: " + vertex + ", edges: " + edge1 + ", " + edge2 + ", dest: " + p1 + "]");
                    return false;
                }
                Point[] ps = Geometry.getPointsAtDistanceWithArea(p1, figure.getOriginalEdgeLengthSquared(edge1),
                                                          p2, figure.getOriginalEdgeLengthSquared(edge2));
                for (Point p : ps) {
                    if (tryToPlace(vertex, p, placed)) {
                        return true;
                    }
                }
                return false;
            default:
                int ei = 0;
                for (;;) {
                    edge1 = edges.get(ei);
                    edge2 = edges.get(ei + 1);
                    assert edge1 != edge2;
                    p1 = figure.getVertex(figure.getEdge(edge1).getOtherVertex(vertex));
                    p2 = figure.getVertex(figure.getEdge(edge2).getOtherVertex(vertex));
                    if (!p1.equals(p2)) {
                        break;
                    }
                    ei++;
                }
                ps = Geometry.getPointsAtDistanceWithArea(p1, figure.getOriginalEdgeLengthSquared(edge1),
                                                  p2, figure.getOriginalEdgeLengthSquared(edge2));
                if (ps.length == 0) {
                    return false;
                }
                for (int i = ei; i < edges.size() - 1; i++) {
                    int e1 = edges.get(i);
                    int e2 = edges.get(i+1);
                    Point q1 = figure.getVertex(figure.getEdge(edge1).getOtherVertex(vertex));
                    Point q2 = figure.getVertex(figure.getEdge(edge2).getOtherVertex(vertex));
                    if (!q2.equals(p1)) {
                        Point[] ps2 = Geometry.getPointsAtDistanceWithArea(q1, figure.getOriginalEdgeLengthSquared(e1),
                                q2, figure.getOriginalEdgeLengthSquared(e2));
                        for (Point p : ps2) {
                            for (Point q : ps) {
                                if (p.equals(q) && tryToPlace(vertex, p, placed)) {
                                    return true;
                                }
                            }
                        }
                    }
                }

                log("No valid placement for vertex " + vertex + " with " + edges.size() + " edges found");
                return false;
        }
    }

    private boolean tryToPlace(int vertex, Point newPosition, Set<Integer> placed) {
        figure.moveVertex(vertex, newPosition);
        BitSet ps = new BitSet();
        int i = 0;
        for (int v : placed) {
            ps.set(v);
        }
        ps.set(vertex);
        boolean success = problem.isValidFor(ps);
        if (DEBUG) {
            log("Placing vertex " + vertex + " at " + newPosition + ": " + success);
        }
        return success;
    }

}
