package model;

import math.BigRational;

public class Lines {

    public static boolean isVertical(Point a, Point b) {
        return a.getX() == b.getX();
    }

    public static boolean isHorizontal(Point a, Point b) {
        return a.getY() == b.getY();
    }

    public static boolean containsPoint(Point a, Point b, Point p) {
        if (a.equals(p) || b.equals(p)) {
            return true;
        }
        long da = a.distanceSquared(p);
        long db = b.distanceSquared(p);
        long dist = a.distanceSquared(b);
        return da <= dist && db <= dist && (Math.sqrt(da) + Math.sqrt(db)) == Math.sqrt(dist);
    }

    /**
     * Line segment slope (&Delta;y / &Delta;x).
     */
    public static BigRational slope(Point p1, Point p2) {
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
        return BigRational.valueOf(dy, dx);
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

    public static Point intersection(Point a1, Point a2, Point b1, Point b2) {
        //we do not want parallel stuff, multiple points etc
        if (isVertical(a1, a2) && isVertical(b1, b2)) return null;
        if ((!isVertical(a1, a2)) && (!isVertical(b1, b2))) {
            if (dblSlope(a1, a2) == dblSlope(b1, b2)) return null;
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
            return null;
        } else if (denom == 0) {
            //parallel
            return null;
        } else {
            BigRational ua = BigRational.valueOf(num_Ua, denom);
            BigRational ub = BigRational.valueOf(num_Ub, denom);
            BigRational x = BigRational.valueOf(x1).add(ua.multiply(BigRational.valueOf(x2 - x1)));
            BigRational y = BigRational.valueOf(y1).add(ua.multiply(BigRational.valueOf(y2 - y1)));
            if (ua.compareTo(BigRational.ZERO) >= 0 && ua.compareTo(BigRational.ONE) <= 0 && ub.compareTo(BigRational.ZERO) >= 0 && ub.compareTo(BigRational.ONE) <= 0) {
                //real intersection

                // but we don't want ends
                if (a1.equals(b1) || a2.equals(b1) || a1.equals(b2) || a2.equals(b2))
                    return null;


                return Point.of(x.longValue(), y.longValue());
            }
            //intersection outside of lines
            return null;
        }
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

                // but we don't want ends
                if (a1.equals(b1) || a2.equals(b1) || a1.equals(b2) || a2.equals(b2))
                    return false;

                return true;
            }
            //intersection outside of lines
            return false;
        }
    }

}
