package model;

public class Points {

    public static Point middle(Point a, Point b) {
        long xsum = a.getX() + b.getX();
        long ysum = a.getY() + b.getY();
        if ((xsum & 1) != 0 || (ysum & 1) != 0)
            return null;
        return Point.of(xsum / 2, ysum / 2);
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt(p1.distanceSquared(p2));
    }

}
