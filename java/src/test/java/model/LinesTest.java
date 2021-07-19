package model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LinesTest {

    @Test
    public void testSubSegmentHorizontal() {
        Point a = Point.of(1, 1);
        Point b = Point.of(10, 1);
        assertTrue(Lines.isSubSegment(a, b, Point.of(2,1), Point.of(6,1)));
        assertTrue(Lines.isSubSegment(a, b, a, b));
        assertTrue(Lines.isSubSegment(a, b, b, a));
        assertFalse(Lines.isSubSegment(a, b, Point.of(0, 1), Point.of(5, 1)));
        assertFalse(Lines.isSubSegment(a, b, Point.of(2,2), Point.of(5,2)));
    }

    @Test
    public void testSubSegmentVertical() {
        Point a = Point.of(1, 1);
        Point b = Point.of(1, 10);

        assertTrue(Lines.isSubSegment(a, b, Point.of(1,2), Point.of(1,6)));
        assertTrue(Lines.isSubSegment(a, b, a, b));
        assertTrue(Lines.isSubSegment(a, b, b, a));
        assertFalse(Lines.isSubSegment(a, b, Point.of(1, 0), Point.of(1, 5)));
        assertFalse(Lines.isSubSegment(a, b, Point.of(2,2), Point.of(2,5)));
    }

    @Test
    public void testSubSegmentDiagonal() {
        assertTrue(Lines.isSubSegment(Point.of(1,1), Point.of(10,10), Point.of(3,3), Point.of(7,7)));
        assertFalse(Lines.isSubSegment(Point.of(1,1), Point.of(5,5), Point.of(3,3), Point.of(7,7)));
    }

    @Test
    public void testOverlapsHorizontal() {
        Point a = Point.of(1, 1);
        Point b = Point.of(10, 1);
        assertTrue(Lines.overlaps(a, b, Point.of(5,1), Point.of(11,1)));
        assertTrue(Lines.overlaps(a, b, Point.of(0,1), Point.of(4,1)));
    }

    @Test
    public void testOverlapsDiagonal() {
        assertTrue(Lines.overlaps(Point.of(1,1), Point.of(5,5), Point.of(7,7), Point.of(3,3)));
        assertTrue(Lines.overlaps(Point.of(1,1), Point.of(5,5), Point.of(3,3), Point.of(7,7)));
        assertFalse(Lines.overlaps(Point.of(1,1), Point.of(10,10), Point.of(3,3), Point.of(7,7)));
    }

}
