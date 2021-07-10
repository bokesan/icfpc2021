package model;

public class Points {

    public static Point middle(Point a, Point b) {
        long xsum = a.getX() + b.getX();
        long ysum = a.getY() + b.getY();
        if ((xsum & 1) != 0 || (ysum & 1) != 0)
            return null;

        return Point.of((a.getX() + b.getX()) / 2, (a.getY() + b.getY()) / 2);
    }

}
