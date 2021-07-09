package math;

import java.math.BigInteger;

class BigRationalBig extends BigRational {

    private final BigInteger num;   // the numerator
    private final BigInteger den;   // the denominator

    public BigInteger getNumerator() { return num; }
    public BigInteger getDenominator() { return den; }
    
    protected BigRationalBig(BigInteger numerator, BigInteger denominator) {
        if (denominator.compareTo(BigInteger.ONE) <= 0) {
            throw new AssertionError("internal error: denominator should be > 1, but is " + denominator);
        }
        assert !numerator.equals(BigInteger.ZERO);
        num = numerator;
        den = denominator;
    }

    // return a * b
    public BigRational multiply(BigRational b) {
        return valueOf(this.num.multiply(b.getNumerator()), this.den.multiply(b.getDenominator()));
    }

    public BigRational square() {
        return valueOf(num.pow(2), den.pow(2));
    }
    
    // return a + b
    public BigRational add(BigRational b) {
        BigInteger b_den = b.getDenominator();
        BigInteger numerator   = num.multiply(b_den).add(b.getNumerator().multiply(den));
        BigInteger denominator = den.multiply(b_den);
        return valueOf(numerator, denominator);
    }

    // return -a
    public BigRational negate() {
        return new BigRationalBig(num.negate(), den);
    }

    // return a - b
    public BigRational subtract(BigRational b) {
        BigInteger b_den = b.getDenominator();
        BigInteger numerator   = num.multiply(b_den).subtract(b.getNumerator().multiply(den));
        BigInteger denominator = den.multiply(b_den);
        return valueOf(numerator, denominator);
    }

    // return 1 / a
    public BigRational reciprocal() {
        return valueOf(den, num);
    }

    @Override
    public BigRational abs() {
        return signum() < 0 ? this.negate() : this;
    }

    // return a / b
    public BigRational divide(BigRational b) {
        return valueOf(this.num.multiply(b.getDenominator()), this.den.multiply(b.getNumerator()));
    }
    
    // return { -1, 0, + 1 } if a < b, a = b, or a > b
    public int compareTo(BigRational b) {
        return num.multiply(b.getDenominator()).compareTo(den.multiply(b.getNumerator()));
    }

    public boolean isIntegral() {
        return false;
    }
    
    public int signum() {
        return num.signum();
    }
    
    public boolean isFixed() {
        return false;
    }
    
    @Override
    protected long getDenominatorFixed() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected long getNumeratorFixed() {
        throw new UnsupportedOperationException();
    }
    
}
