package model;

public class Lines {

    public static boolean isVertical(Point a, Point b) {
        return a.getX() == b.getX();
    }

    public static boolean isHorizontal(Point a, Point b) {
        return a.getY() == b.getY();
    }

    public static boolean containsPoint(Point a, Point b, int x, int y) {
        if (a.isEqual(x, y) || b.isEqual(x, y)) {
            return true;
        }
        long da = a.distanceSquared(x, y);
        long db = b.distanceSquared(x, y);
        long dist = a.distanceSquared(b);
        return da <= dist && db <= dist && (Math.sqrt(da) + Math.sqrt(db)) == Math.sqrt(dist);
    }

    /**
     * Line segment slope (&Delta;y / &Delta;x).
     */
    public static double dblSlope(Point p1, Point p2) {
        Point from;
        Point to;
        if (p1.getX() < p2.getX() || (p1.getX() == p2.getX() && p1.getY() <= p2.getY())) {
            from = p1;
            to = p2;
        } else {
            from = p2;
            to = p1;
        }

        // dy / dx
        long dx = to.getX() - from.getX();
        if (dx == 0) {
            throw new UnsupportedOperationException("slope is not defined for vertical lines [" + from + ", " + to + "]");
        }
        long dy = to.getY() - from.getY();
        return dy / (double) dx;
    }

    public static boolean intersect(Point a1, Point a2, Point b1, Point b2) {
        return intersection_dbl(a1, a2, b1, b2);
    }

    public static boolean intersection_dbl(Point a1, Point a2, Point b1, Point b2) {
        //we do not want parallel stuff, multiple points etc
        if (isVertical(a1, a2) && isVertical(b1, b2)) return false;
        if ((!isVertical(a1, a2)) && (!isVertical(b1, b2))) {
            if (dblSlope(a1, a2) == dblSlope(b1, b2)) return false;
        }

        long x1 = a1.getX();
        long y1 = a1.getY();
        long x2 = a2.getX();
        long y2 = a2.getY();
        long x3 = b1.getX();
        long y3 = b1.getY();
        long x4 = b2.getX();
        long y4 = b2.getY();

        long denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        long d1 = y1 - y3;
        long d2 = x1 - x3;
        long num_Ua = (x4 - x3) * d1 - d2 * (y4 - y3);
        long num_Ub = (x2 - x1) * d1 - d2 * (y2 - y1);

        if (denom == 0 && num_Ua == 0 && num_Ub == 0) {
            //coincident
            return false;
        } else if (denom == 0) {
            //parallel
            return false;
        } else {
            double ua = num_Ua / (double) denom;
            double ub = num_Ub / (double) denom;
            if (ua >= 0 && ua <= 1 && ub >= 0 && ub <= 1) {
                //real intersection

                double x = x1 + ua * (x2 - x1);
                double y = y1 + ua * (y2 - y1);

                // but we don't want ends
                if (eq(a1, x, y) || eq(a2, x, y) || eq(b1, x, y) || eq(b2, x, y))
                    return false;

                return true;
            }
            //intersection outside of lines
            return false;
        }
    }

    public static boolean isSubSegment(Point a1, Point a2, Point b1, Point b2) {
        long x1 = a1.getX();
        long y1 = a1.getY();
        long x2 = a2.getX();
        long y2 = a2.getY();
        long x3 = b1.getX();
        long y3 = b1.getY();
        long x4 = b2.getX();
        long y4 = b2.getY();
        if (isVertical(a1, a2) && isVertical(b1, b2)) {
            return x1 == x3 &&
                    Math.max(y3, y4) <= Math.max(y1, y2) &&
                    Math.min(y3, y4) >= Math.min(y1, y2);
        }
        if (isHorizontal(a1, a2) && isHorizontal(b1, b2)) {
            return y1 == y3 &&
                    Math.max(x3, x4) <= Math.max(x1, x2) &&
                    Math.min(x3, x4) >= Math.min(x1, x2);
        }

        long denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        long d1 = y1 - y3;
        long d2 = x1 - x3;
        long num_Ua = (x4 - x3) * d1 - d2 * (y4 - y3);
        long num_Ub = (x2 - x1) * d1 - d2 * (y2 - y1);

        return denom == 0 && num_Ua == 0 && num_Ub == 0 &&
                Math.max(x3, x4) <= Math.max(x1, x2) &&
                Math.min(x3, x4) >= Math.min(x1, x2);
    }

    public static boolean overlaps(Point a1, Point a2, Point b1, Point b2) {
        long x1 = a1.getX();
        long y1 = a1.getY();
        long x2 = a2.getX();
        long y2 = a2.getY();
        long x3 = b1.getX();
        long y3 = b1.getY();
        long x4 = b2.getX();
        long y4 = b2.getY();
        if (isVertical(a1, a2) && isVertical(b1, b2)) {
            return x1 == x3 &&
                    Math.max(y3, y4) >= Math.min(y1, y2) &&
                    Math.min(y3, y4) <= Math.max(y1, y2);
        }
        if (isHorizontal(a1, a2) && isHorizontal(b1, b2)) {
            return y1 == y3 &&
                    Math.max(x3, x4) >= Math.min(x1, x2) &&
                    Math.min(x3, x4) <= Math.max(x1, x2);
        }

        long denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        long d1 = y1 - y3;
        long d2 = x1 - x3;
        long num_Ua = (x4 - x3) * d1 - d2 * (y4 - y3);
        long num_Ub = (x2 - x1) * d1 - d2 * (y2 - y1);

        long startA = Math.min(x1, x2);
        long endA = Math.max(x1, x2);
        long startB = Math.min(x3, x4);
        long endB = Math.max(x3, x4);

        return denom == 0 && num_Ua == 0 && num_Ub == 0 &&
                ((startB <= endA && endA < endB) || (startB < startA && startA <= endB));
    }

    private static boolean eq(Point p, double x, double y) {
        return p.getX() == x && p.getY() == y;
    }
}
