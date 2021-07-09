package model;

public class Bounds {

    private final long minX;
    private final long maxX;
    private final long minY;
    private final long maxY;

    private Bounds(long minX, long maxX, long minY, long maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public static Bounds of(Point[] points) {
        long minX = Long.MAX_VALUE;
        long maxX = Long.MIN_VALUE;
        long minY = Long.MAX_VALUE;
        long maxY = Long.MIN_VALUE;
        for (Point p : points) {
            minX = Math.min(minX, p.getX());
            maxX = Math.max(maxX, p.getX());
            minY = Math.min(minY, p.getY());
            maxY = Math.max(maxY, p.getY());
        }
        return new Bounds(minX, maxX, minY, maxY);
    }

    public Bounds union(Bounds b) {
        return new Bounds(Math.min(minX, b.minX), Math.max(maxX, b.maxX),
                Math.min(minY, b.minY), Math.max(maxY, b.maxY));
    }

    public long getMinX() {
        return minX;
    }

    public long getMaxX() {
        return maxX;
    }

    public long getMinY() {
        return minY;
    }

    public long getMaxY() {
        return maxY;
    }

    public long getWidth() {
        return maxX - minX;
    }

    public long getHeight() {
        return maxY - minY;
    }

    public Point getCenter() {
        long cx = (minX + maxX) / 2;
        long cy = (minY + maxY) / 2;
        return Point.of(cx, cy);
    }

    @Override
    public String toString() {
        return "Bounds{" +
                "minX=" + minX +
                ", maxX=" + maxX +
                ", minY=" + minY +
                ", maxY=" + maxY +
                '}';
    }
}
