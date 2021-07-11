package model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static model.Polygon.toJson;

public class Pose {

    private final Point[] vertices;

    public Pose(Point[] vertices) {
        this.vertices = Arrays.copyOf(vertices, vertices.length);
    }

    public Pose(Collection<Point> vertices) {
        this.vertices = vertices.toArray(new Point[0]);
    }

    private static Pose of(JsonNode json) {
        if (!json.isObject()) {
            throw new IllegalArgumentException("JSON object expected, but got " + json);
        }
        List<Point> vertices = new ArrayList<>();
        for (JsonNode vertex : json.get("vertices")) {
            vertices.add(Point.of(vertex));
        }
        return new Pose(vertices);
    }

    public static Pose loadFromFile(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(new File(fileName));
        return of(json);
    }

    public int getNumVertices() {
        return vertices.length;
    }

    public Point[] getVertices() {
        return vertices;
    }

    @Override
    public String toString() {
        return "{ \"vertices\": " + toJson(vertices) + " }";
    }
}
