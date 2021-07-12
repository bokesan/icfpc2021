package model;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GeometryTest {

    @Test
    public void testGetPointsAtDistance_horizontal_too_far() {
        Point p1 = Point.origin();
        Point p2 = Point.of(10, 0);
        Point[] solution = Geometry.getPointsAtDistance(p1, 16, p2, 25);
        assertEquals(0, solution.length);
    }

    @Test
    public void testGetPointsAtDistance_horizontal_mid() {
        Point p1 = Point.origin();
        Point p2 = Point.of(10, 0);
        Point[] solution = Geometry.getPointsAtDistance(p1, 25, p2, 25);
        System.out.println("mid1: " + Arrays.toString(solution));
        assertEquals(1, solution.length);
        assertEquals(Point.of(5, 0), solution[0]);

        solution = Geometry.getPointsAtDistance(p1, 9, p2, 49);
        assertEquals(1, solution.length);
        assertEquals(Point.of(3, 0), solution[0]);
    }

    @Test
    public void testGetPointsAtDistance_horizontal_2() {
        Point p1 = Point.origin();
        Point p2 = Point.of(10, 0);
        Point[] solution = Geometry.getPointsAtDistance(p1, 49, p2, 49);
        assertEquals(2, solution.length);
        long expectedY = Math.round(2 * Math.sqrt(6));
        Point p3_1;
        Point p3_2;
        if (solution[0].getY() < 0) {
            p3_1 = solution[1];
            p3_2 = solution[0];
        } else {
            p3_1 = solution[0];
            p3_2 = solution[1];
        }
        assertEquals(Point.of(5, expectedY), p3_1);
        assertEquals(Point.of(5, -expectedY), p3_2);
    }

    @Test
    public void testFlipSimpleH() {
        Point a = Point.of(0, 1);
        Point b = Point.of(2, 1);
        Point p = Point.of(4, 2);
        Point p1 = Geometry.flip(a, b, p);
        assertEquals(4, p1.getX());
        assertEquals(0, p1.getY());
    }

    @Test
    public void testFlipSimpleV() {
        Point a = Point.of(2, 1);
        Point b = Point.of(2, 10);
        Point p = Point.of(0, 5);
        Point p1 = Geometry.flip(a, b, p);
        assertEquals(4, p1.getX());
        assertEquals(5, p1.getY());
    }

    @Test
    public void testFlipGeneral() {
        Point a = Point.origin();
        Point b = Point.of(20,20);
        Point p = Point.of(0, 1);
        Point p1 = Geometry.flip(a, b, p);
        assertEquals(Point.of(1, 0), p1);
    }

}
