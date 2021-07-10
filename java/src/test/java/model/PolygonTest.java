package model;

import math.BigRational;
import org.junit.Test;

import static org.junit.Assert.*;

public class PolygonTest {

    private static final Point P10 = Point.of(2, 0);
    private static final Point P11 = Point.of(2, 2);
    private static final Point P01 = Point.of(0, 2);
    private static final Point TOP = Point.of(1, 2);
    private static final Point CENTER = Point.of(1, 1);

    @Test
    public void testGetBoundingBox() {
        Polygon p = Polygon.of(new Point[]{Point.origin(), P10, TOP});
        Bounds r = p.getBounds();
        assertEquals(0, r.getMinX());
        assertEquals(0, r.getMinY());
        assertEquals(2, r.getMaxX());
        assertEquals(2, r.getMaxY());
    }

    @Test
    public void testOrientationCCW() {
        Polygon p = Polygon.of(new Point[]{Point.origin(), P10, TOP});
        assertFalse(p.isClockwise());
    }

    @Test
    public void testOrientationCW() {
        Polygon p = Polygon.of(new Point[]{Point.origin(), TOP, P10});
        assertTrue(p.isClockwise());
    }

    @Test
    public void testAreaSimple() {
        Polygon p = Polygon.UNIT_SQUARE_ORIGIN;
        assertEquals(BigRational.ONE, p.area());
    }

    private static Polygon fromPoints(int[] points) {
        Point[] pts = new Point[points.length / 2];
        for (int i = 0; i < pts.length; i++) {
            pts[i] = Point.of(points[2*i], points[2*i+1]);
        }
        return Polygon.of(pts);
    }

    @Test
    public void testArea2() {
        Polygon p = fromPoints(new int[]{1,0, 1,1, 2,2, 2,1});
        assertEquals(BigRational.ONE, p.area());
    }

    @Test
    public void testTurnsLeft() {
        assertTrue(Polygon.turnsLeft(Point.origin(), P10, TOP));
        assertTrue(Polygon.turnsLeft(P10, TOP, Point.origin()));
        assertTrue(Polygon.turnsLeft(TOP, Point.origin(), P10));

        assertFalse(Polygon.turnsLeft(Point.origin(), TOP, P10));
        assertFalse(Polygon.turnsLeft(TOP, P10, Point.origin()));
        assertFalse(Polygon.turnsLeft(P10, Point.origin(), TOP));

        assertTrue(Polygon.turnsLeft(TOP, P01, Point.origin()));
        assertTrue(Polygon.turnsLeft(P01, Point.origin(), TOP));
        assertTrue(Polygon.turnsLeft(Point.origin(), TOP, P01));

        assertFalse(Polygon.turnsLeft(Point.origin(), P01, P11));
        assertFalse(Polygon.turnsLeft(P01, P11, Point.origin()));
        assertFalse(Polygon.turnsLeft(P11, Point.origin(), P01));

        assertFalse(Polygon.turnsLeft(P01, CENTER, Point.origin()));
    }

    @Test
    public void testIsConvex() {
        Polygon p = Polygon.of(new Point[]{Point.origin(), P10, TOP});
        assertTrue(p.isConvex());
        p = Polygon.of(new Point[]{Point.origin(), TOP, P10});
        assertTrue(p.isConvex());
        p = Polygon.of(new Point[]{Point.origin(), P10, P11, P01});
        assertTrue(p.isConvex());
        p = Polygon.of(new Point[]{Point.origin(), P10, P11, CENTER, P01});
        assertFalse(p.isConvex());
        p = Polygon.of(new Point[]{Point.origin(), CENTER, P10, P11, P01});
        assertFalse(p.isConvex());
        p = Polygon.of(new Point[]{CENTER, Point.origin(), P10, P11, P01});
        assertFalse(p.isConvex());
        p = Polygon.of(new Point[]{Point.origin(), P10, CENTER});
        assertTrue(p.isConvex());
    }

    @Test
    public void testContainsLine1() {
        Polygon p = fromPoints(new int[]{1,1, 1,2, 2,3, 1,4, 1,5, 5,5, 5,1});
        assertTrue(p.containsEdge(Point.of(3,3), Point.of(4,4)));
        assertTrue(p.containsEdge(Point.of(5,2), Point.of(5,4)));
        assertFalse(p.containsEdge(Point.of(1,1), Point.of(1,5)));
        assertFalse(p.containsEdge(Point.of(1,2), Point.of(1,4)));
        assertTrue(p.containsEdge(Point.of(2,3), Point.of(3,3)));
    }

    @Test
    public void testContainsLine2() {
        Polygon p = fromPoints(new int[]{0,0, 10,0, 10,10});
        assertTrue(p.containsEdge(Point.of(10,10), Point.of(9,4)));
        assertTrue(p.containsEdge(Point.of(6,5), Point.of(7,5)));
        assertTrue(p.containsEdge(Point.of(5,5), Point.of(6,5)));
        assertFalse(p.containsEdge(Point.of(4,5),Point.of(6,5)));
    }

}