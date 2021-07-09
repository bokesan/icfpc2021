package math;

import java.math.BigInteger;

class BigRationalIntegral extends BigRational {

    private final BigInteger num;
    
    protected BigRationalIntegral(BigInteger val) {
        num = val;
    }
    
    public boolean isFixed() {
        return false;
    }
   
    public BigInteger getNumerator() {
        return num;
    }
    
    public BigInteger getDenominator() {
        return BigInteger.ONE;
    }
    
    public boolean isIntegral() {
        return true;
    }
    
    public int signum() {
        return num.signum();
    }

    @Override
    public BigRational abs() {
        return signum() < 0 ? negate() : this;
    }
    
    
    // return { -1, 0, + 1 } if a < b, a = b, or a > b
    public int compareTo(BigRational b) {
        return num.multiply(b.getDenominator()).compareTo(b.getNumerator());
    }

    // return a * b
    public BigRational multiply(BigRational b) {
        return valueOf(num.multiply(b.getNumerator()), b.getDenominator());
    }
    
    public BigRational square() {
        return valueOf(num.pow(2));
    }

    // return a + b
    public BigRational add(BigRational b) {
        BigInteger b_den = b.getDenominator();
        BigInteger numerator = num.multiply(b_den).add(b.getNumerator());
        return valueOf(numerator, b_den);
    }

    public BigRational negate() {
        BigInteger n1 = num.negate();
        if (n1.equals(BigInteger.ONE))
            return ONE;
        return new BigRationalIntegral(n1);
    }
    
    public BigRational reciprocal() {
        return valueOf(BigInteger.ONE, num);
    }

    // return a - b
    public BigRational subtract(BigRational b) {
        BigInteger b_den = b.getDenominator();
        BigInteger numerator = num.multiply(b_den).subtract(b.getNumerator());
        return valueOf(numerator, b_den);
    }

    // return a / b
    public BigRational divide(BigRational b) {
        return valueOf(num.multiply(b.getDenominator()), b.getNumerator());
    }
    
    @Override
    protected long getDenominatorFixed() {
        return 1;
    }
    
    @Override
    protected long getNumeratorFixed() {
        throw new UnsupportedOperationException();
    }

}
