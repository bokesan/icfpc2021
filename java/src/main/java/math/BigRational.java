package math;

import java.math.BigInteger;


public abstract class BigRational implements Comparable<BigRational> {

    public static final BigRational ZERO = new BigRationalZero();
    public static final BigRational ONE = new BigRationalOne();

    /** A value slightly larger than sqrt(2). */
    public static final BigRational SQRT2_CEIL = valueOf(665857, 470832);

    public static BigRational valueOf(BigInteger numerator, BigInteger denominator) {
        // deal with x / 0
        int den_signum = denominator.signum();
        
        if (den_signum == 0) {
           throw new IllegalArgumentException("Denominator is zero");
        } else if (den_signum < 0) {
            denominator = denominator.negate();
            numerator = numerator.negate();
        }
        
        int num_signum = numerator.signum();
        if (num_signum == 0)
            return ZERO;

        boolean den_one = denominator.equals(BigInteger.ONE);
        boolean num_one = numerator.equals(BigInteger.ONE);
        
        // reduce fraction if necessary
        if (!(num_one || den_one)) {
            BigInteger g = numerator.gcd(denominator);
            assert g.signum() == 1;

            if (!g.equals(BigInteger.ONE)) {
                numerator = numerator.divide(g);
                denominator = denominator.divide(g);
                den_one = denominator.equals(BigInteger.ONE);
                num_one = numerator.equals(BigInteger.ONE);
                
            }
        }

        if (den_one) {
            if (num_one)
                return ONE;
            return new BigRationalIntegral(numerator);
        }
        return new BigRationalBig(numerator, denominator);
    }
    
    abstract protected BigInteger getNumerator();
    abstract protected BigInteger getDenominator();
    abstract protected long getNumeratorFixed();
    abstract protected long getDenominatorFixed();
    
    abstract protected boolean isIntegral();
    
    abstract protected boolean isFixed();

    public static BigRational valueOf(double d) {
        boolean negative = false;
        long den = 1;
        if (d < 0) {
            negative = true;
            d = -d;
        }
        int loops = 100;
        while (d != (long) d) {
            den *= 2;
            d *= 2;
            if (--loops == 0)
                throw new RuntimeException("can't convert double to BigRational: " + d);
        }
        if (negative)
            return valueOf((long) d, den).negate();
        return valueOf((long) d, den);
    }

    // create and initialize a new BigRational object
    public static BigRational valueOf(long numerator, long denominator) {
        // deal with x / 0
        if (denominator == 0) {
           throw new IllegalArgumentException("Denominator is zero");
        } else if (denominator < 0) {
            denominator = -denominator;
            numerator = -numerator;
        }
        
        if (numerator == 0)
            return ZERO;

        // reduce fraction if necessary
        if (!(numerator == 1 || denominator == 1)) {
            long g = gcd(numerator, denominator);
            if (g != 1) {
                numerator /= g;
                denominator /= g;
            }
        }

        if (denominator == 1) {
            if (numerator == 1)
                return ONE;
            return BigRationalIntegral.valueOf(numerator);
        }
        return new BigRationalBig(BigInteger.valueOf(numerator), BigInteger.valueOf(denominator));
    }
    
    // create and initialize a new BigRational object
    public static BigRational valueOf(BigInteger numerator) {
        if (numerator.signum() == 0)
            return ZERO;
        if (numerator.equals(BigInteger.ONE))
            return ONE;
        return new BigRationalIntegral(numerator);
    }

    // create and initialize a new BigRational object
    public static BigRational valueOf(long numerator) {
        if (numerator == 0)
            return ZERO;
        if (numerator == 1)
            return ONE;
        return new BigRationalIntegral(BigInteger.valueOf(numerator));
    }

    // create and initialize a new BigRational object from a string, e.g., "-343/1273"
    public static BigRational valueOf(String s) {
        String[] tokens = s.split("/");
        if (tokens.length == 2)
            return valueOf(new BigInteger(tokens[0]), new BigInteger(tokens[1]));
        if (tokens.length == 1)
            return valueOf(new BigInteger(tokens[0]));
        throw new IllegalArgumentException("Parse error in BigRational: s");
    }
    

    // return string representation of (this)
    public String toString() { 
        if (isIntegral())
            return getNumerator().toString();
        return getNumerator() + "/" + getDenominator();
    }

    // return { -1, 0, + 1 } if a < b, a = b, or a > b
    abstract public int compareTo(BigRational b);

    public BigRational min(BigRational b) {
        return (this.compareTo(b) <= 0) ? this : b;
    }

    public BigRational max(BigRational b) {
        return (this.compareTo(b) >= 0) ? this : b;
    }
    
    public boolean isZero() {
        return this == ZERO;
    }
    
    abstract public int signum();
    
    @Override
    public boolean equals(Object y) {
        if (!(y instanceof BigRational))
            return false;
        BigRational b = (BigRational) y;
        return getNumerator().equals(b.getNumerator()) && getDenominator().equals(b.getDenominator());
    }
        
    // hashCode consistent with equals() and compareTo()
    public int hashCode() {
        return getNumerator().hashCode() * 13 + getDenominator().hashCode();
    }
    

    abstract public BigRational add(BigRational b);
    abstract public BigRational subtract(BigRational b);
    abstract public BigRational multiply(BigRational b);
    abstract public BigRational divide(BigRational b);
    abstract public BigRational negate();
    abstract public BigRational reciprocal();
    abstract public BigRational square();
    abstract public BigRational abs();
    

    public double doubleValue() {
        return getNumerator().doubleValue() / getDenominator().doubleValue();
    }
    
    public float floatValue() {
        return (float) doubleValue();
    }
    
    public int intValue() {
        return (int) doubleValue();
    }
    
    protected static boolean representableAsInt(BigInteger n) {
        return n.bitLength() <= 31;
    }
    
    private static long gcd(long a, long b) {
        while (b != 0) {
            long t = b;
            b = Math.floorMod(a,b);
            a = t;
        }
        return a;
    }
}
