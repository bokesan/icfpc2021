package model;

import static java.lang.Math.sqrt;

public class Geometry {

    public static Point[] getPointsAtDistance(Point p1, long a, Point p2, long b) {
        if (p1.equals(p2)) {
            throw new IllegalArgumentException("Identical points");
        }

        double dist = sqrt(p1.distanceSquared(p2));
        double d1 = sqrt(a);
        double delta = dist - (d1 + sqrt(b));
        if (delta > 0) {
            // no solution
            return new Point[0];
        }
        if (delta == 0) {
            long dx = p2.getX() - p1.getX();
            long dy = p2.getY() - p1.getY();
            double scale = d1 / dist;
            Point p = p1.translate(Math.round(scale * dx), Math.round(scale * dy));
            return new Point[]{p};
        }

        long p = p1.getX();
        long q = p1.getY();
        long r = p2.getX();
        long s = p2.getY();

        if (q == s && p != r) {
            long term1 = -a + b + p * p - r * r;
            double term2 = 2 * sqrt((r * term1)/ (double) (p - r) - sq(term1) / (4.0 * sq(p - r)) + b -r*r);
            double x = term1 / (2.0 * (p - r));
            double y1 = 0.5 * (2 * s - term2);
            double y2 = 0.5 * (2 * s + term2);
            Point r1 = Point.of(Math.round(x), Math.round(y1));
            Point r2 = Point.of(Math.round(x), Math.round(y2));
            return new Point[]{r1, r2};
        } else {
            double x1 = compX(true, a, b, p, q, r, s);
            double y1 = compY(true, a, b, p, q, r, s);
            double x2 = compX(false, a, b, p, q, r, s);
            double y2 = compY(false, a, b, p, q, r, s);
            Point r1 = Point.of(Math.round(x1), Math.round(y1));
            Point r2 = Point.of(Math.round(x2), Math.round(y2));
            return new Point[]{r1, r2};
        }
    }

    public static Point[] getPointsAtDistanceWithArea(Point p1, long a, Point p2, long b) {
        if (p1.equals(p2)) {
            throw new IllegalArgumentException("Identical points");
        }

        double dist = sqrt(p1.distanceSquared(p2));
        double d1 = sqrt(a);
        double delta = dist - (d1 + sqrt(b));
        if (delta > 0) {
            // no solution
            return new Point[0];
        }
        if (delta == 0) {
            long dx = p2.getX() - p1.getX();
            long dy = p2.getY() - p1.getY();
            double scale = d1 / dist;
            Point p = p1.translate(Math.round(scale * dx), Math.round(scale * dy));
            return new Point[]{p};
        }

        long p = p1.getX();
        long q = p1.getY();
        long r = p2.getX();
        long s = p2.getY();

        if (q == s && p != r) {
            long term1 = -a + b + p * p - r * r;
            double term2 = 2 * sqrt((r * term1)/ (double) (p - r) - sq(term1) / (4.0 * sq(p - r)) + b -r*r);
            double x = term1 / (2.0 * (p - r));
            double y1 = 0.5 * (2 * s - term2);
            double y2 = 0.5 * (2 * s + term2);
            return allPoints(x, y1, y2);
            // Point r1 = Point.of(Math.round(x), Math.round(y1));
            // Point r2 = Point.of(Math.round(x), Math.round(y2));
            // return new Point[]{r1, r2};
        } else {
            double x1 = compX(true, a, b, p, q, r, s);
            double y1 = compY(true, a, b, p, q, r, s);
            double x2 = compX(false, a, b, p, q, r, s);
            double y2 = compY(false, a, b, p, q, r, s);
            return allPoints(x1, x2, y1, y2);
            // Point r1 = Point.of(Math.round(x1), Math.round(y1));
            // Point r2 = Point.of(Math.round(x2), Math.round(y2));
            // return new Point[]{r1, r2};
        }
    }

    private static Point[] allPoints(double x, double y1, double y2) {
        long x1 = (long) Math.floor(x);
        long x2 = (long) Math.ceil(x);
        long y11 = (long) Math.floor(y1);
        long y12 = (long) Math.ceil(y1);
        long y21 = (long) Math.floor(y2);
        long y22 = (long) Math.ceil(y2);
        return new Point[]{Point.of(x1, y11), Point.of(x1, y12), Point.of(x1, y21), Point.of(x1, y22),
                Point.of(x2, y11), Point.of(x2, y12), Point.of(x2, y21), Point.of(x2, y22)};
    }

    private static Point[] allPoints(double x1, double x2, double y1, double y2) {
        long x11 = (long) Math.floor(x1);
        long x12 = (long) Math.ceil(x1);
        long x21 = (long) Math.floor(x2);
        long x22 = (long) Math.ceil(x2);
        long y11 = (long) Math.floor(y1);
        long y12 = (long) Math.ceil(y1);
        long y21 = (long) Math.floor(y2);
        long y22 = (long) Math.ceil(y2);
        return new Point[]{Point.of(x11, y11), Point.of(x11, y12), Point.of(x11, y21), Point.of(x11, y22),
                Point.of(x12, y11), Point.of(x12, y12), Point.of(x12, y21), Point.of(x12, y22),
                Point.of(x21, y11), Point.of(x21, y12), Point.of(x21, y21), Point.of(x21, y22),
                Point.of(x22, y11), Point.of(x22, y12), Point.of(x22, y21), Point.of(x22, y22)
        };
    }

    private static long sq(long x) {
        return x * x;
    }

    private static double sq(double x) {
        return x * x;
    }

    private static double compX(boolean first, double a, double b, double p, double q, double r, double s) {
        double sign = first ? -1 : 1;
        return (sign * sqrt(
                        -sq(q - s)
                                * (a*a
                                - 2.0 * a * b
                                - 2 * a * p * p
                                + 4 * a * p * r
                                - 2 * a * q * q
                                + 4 * a * q * s
                                - 2 * a * r * r
                                - 2 * a * s * s
                                + b * b
                                - 2 * b * p * p
                                + 4 * b * p * r
                                - 2 * b * q * q
                                + 4 * b * q * s
                                - 2 * b * r * r
                                - 2 * b * s * s
                                + p * p * p * p
                                - 4 * p * p * p * r
                                + 2 * p * p * q * q
                                - 4 * p * p * q * s
                                + 6 * p * p * r * r
                                + 2 * p * p * s * s
                                - 4 * p * q * q * r
                                + 8 * p * q * r * s
                                - 4 * p * r * r * r
                                - 4 * p * r * s * s
                                + q * q * q * q
                                - 4 * q * q * q * s
                                + 2 * q * q * r * r
                                + 6 * q * q * s * s
                                - 4 * q * r * r * s
                                - 4 * q * s * s * s
                                + r * r * r * r
                                + 2.0 * r * r * s * s
                                + s * s * s * s)
                )
                        - a * p
                        + a * r
                        + b * p
                        - b * r
                        + p * p * p
                        - p * p * r
                        + p * q * q
                        - 2.0 * p * q * s
                        - p * r * r
                        + p * s * s
                        + q * q * r
                        - 2.0 * q * r * s
                        + r * r * r
                        + r * s * s)
                        / (2.0 * (p * p
                        - 2.0 * p * r
                        + q * q
                        - 2 * q * s
                        + r * r
                        + s * s)
                );
    }

    private static double compY(boolean first, double a, double b, double p, double q, double r, double s) {
        double sign = first ? 1 : -1;
        double term1 = a * a
                - 2.0 * a * b
                - 2 * a * p * p
                + 4 * a * p * r
                - 2 * a * q * q
                + 4 * a * q * s
                - 2 * a * r * r
                - 2 * a * s * s
                + b * b
                - 2 * b * p * p
                + 4 * b * p * r
                - 2 * b * q * q
                + 4 * b * q * s
                - 2 * b * r * r
                - 2 * b * s * s
                + p * p * p * p
                - 4 * p * p * p * r
                + 2 * p * p * q * q
                - 4 * p * p * q * s
                + 6 * p * p * r * r
                + 2 * p * p * s * s
                - 4 * p * q * q * r
                + 8 * p * q * r * s
                - 4 * p * r * r * r
                - 4 * p * r * s * s
                + q * q * q * q
                - 4 * q * q * q * s
                + 2 * q * q * r * r
                + 6 * q * q * s * s
                - 4 * q * r * r * s
                - 4 * q * s * s * s
                + r * r * r * r
                + 2 * r * r * s * s
                + s * s * s * s;
        return
         (sign * p * sqrt(
                                -sq(q - s)
                                * term1)
                - sign * r * sqrt(-sq(q - s) *
                 term1)
        - a * q*q
                + 2 * a * q * s
                - a * s*s
                + b * q*q
                - 2 * b * q * s
                + b * s*s
                + p*p * q*q
                - p*p * s*s
                - 2 * p * q*q * r
                + 2 * p * r * s*s
                + q*q*q*q
                - 2 * q*q*q * s
                + q*q * r*r
                + 2 * q * s*s*s
                - r*r * s*s
                - s*s*s*s) / (2.0 * (q - s) * (p*p - 2 * p * r + q*q - 2 * q * s + r*r + s*s));
    }

    /**
     * Flip p about the line given by a and b.
     */
    public static Point flip(Point a, Point b, Point p) {
        if (a.getX() == b.getX()) {
            return p.flipHorizontal(a.getX());
        }
        if (a.getY() == b.getY()) {
            return p.flipVertical(a.getY());
        }

        double dx  = (double) (b.getX() - a.getX());
        double dy  = (double) (b.getY() - a.getY());

        double a1   = (dx*dx - dy*dy) / (dx*dx + dy*dy);
        double b1   = 2 * dx * dy / (dx*dx + dy*dy);

        long x2 = Math.round(a1 * (p.getX() - a.getX()) + b1 * (p.getY() - a.getY()) + a.getX());
        long y2 = Math.round(b1 * (p.getX() - a.getX()) - a1 * (p.getY() - a.getY()) + a.getY());

        return Point.of(x2,y2);

        /*
        Point[] ps = getPointsAtDistance(a, p.distanceSquared(a), b, p.distanceSquared(b));
        if (ps.length != 2) {
            return null;
        }
        if (ps[0].equals(p)) {
            return ps[1];
        }
        return ps[0];

         */
    }
}
