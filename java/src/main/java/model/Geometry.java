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


    private static long sq(long x) {
        return x * x;
    }

    private static double compX(boolean first, long a, long b, long p, long q, long r, long s) {
        double sign = first ? -1 : 1;
        return (sign * sqrt(
                        -sq(q - s)
                                * (a*a
                                - 2 * a * b
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

    private static double compY(boolean first, long a, long b, long p, long q, long r, long s) {
        double sign = first ? 1 : -1;
        long term1 = a * a
                - 2 * a * b
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
}
