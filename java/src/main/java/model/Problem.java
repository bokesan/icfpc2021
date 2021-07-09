package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class Problem {

    private final Hole hole;
    private final Figure figure;
    private final long epsilon;

    public Problem(Hole hole, Figure figure, long epsilon) {
        this.hole = hole;
        this.figure = figure;
        this.epsilon = epsilon;
    }

    public static Problem of(JsonNode node) {
        Hole hole = Hole.of(node.get("hole"));
        Figure figure = Figure.of(node.get("figure"));
        long epsilon = node.get("epsilon").longValue();
        return new Problem(hole, figure, epsilon);
    }

    public static Problem loadFromFile(String problemFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(new File(problemFile));
        return of(json);
    }

    public Hole getHole() {
        return hole;
    }

    public Figure getFigure() {
        return figure;
    }

    public long getEpsilon() {
        return epsilon;
    }

    public Bounds getBounds() {
        return hole.getBounds().union(figure.getBounds());
    }

    @Override
    public String toString() {
        return "Problem{" +
                "hole=" + hole +
                ", figure=" + figure +
                ", epsilon=" + epsilon +
                '}';
    }
}
