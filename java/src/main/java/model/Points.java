package model;

public class Points {

    public static Point geometricCenter(Point[] points) {
        int n = points.length;
        long sumX = 0;
        long sumY = 0;
        for (Point p : points) {
            sumX += p.getX();
            sumY += p.getY();
        }
        return Point.of(sumX / n, sumY / n);
    }

    public static Point middle(Point a, Point b) {
        return Point.of((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
    }

}
