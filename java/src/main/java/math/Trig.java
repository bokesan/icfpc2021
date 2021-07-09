package math;

public class Trig {

    public static BigRational sin(BigRational deg) {
        deg = normalizeDegrees(deg);
        if (deg.compareTo(BigRational.valueOf(90)) > 0) {
            deg = BigRational.valueOf(180).subtract(deg);
        }

        if (deg.equals(BigRational.ZERO)) {
            return BigRational.ZERO;
        }
        if (deg.equals(BigRational.valueOf(30))) {
            return BigRational.valueOf(1,2);
        }
        if (deg.equals(BigRational.valueOf(45))) {
            return BigRational.SQRT2_CEIL.reciprocal();
        }
        if (deg.equals(BigRational.valueOf(90))) {
            return BigRational.ONE;
        }
        /*
        // Bhaskara I's sine approximation formula
        BigRational d1 = BigRational.valueOf(180).subtract(deg).multiply(deg);
        return BigRational.valueOf(4).multiply(d1)
                .divide(BigRational.valueOf(40500).subtract(d1));
        */
        double dv = deg.doubleValue();
        return BigRational.valueOf(Math.sin(Math.toRadians(dv)));
    }

    public static BigRational cos(BigRational deg) {
        return sin(deg.add(BigRational.valueOf(90)));
    }

    public static BigRational tan(BigRational deg) {
        return sin(deg).divide(cos(deg));
    }

    public static BigRational atan2(BigRational y, BigRational x) {
        return BigRational.valueOf(atan2asDouble(y, x));
    }

    public static double atan2asDouble(BigRational y, BigRational x) {
        return Math.toDegrees(Math.atan2(y.doubleValue(), x.doubleValue()));
    }

    private static BigRational normalizeDegrees(BigRational deg) {
        while (deg.signum() < 0) {
            deg = deg.add(BigRational.valueOf(180));
        }
        while (deg.compareTo(BigRational.valueOf(180)) >= 0) {
            deg = deg.subtract(BigRational.valueOf(180));
        }
        return deg;
    }
}
