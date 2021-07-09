package math;

import java.math.BigInteger;


class BigRationalZero extends BigRational {

    public boolean isFixed() {
        return true;
    }
    
    public BigInteger getNumerator() {
        return BigInteger.ZERO;
    }
    
    public BigInteger getDenominator() {
        return BigInteger.ONE;
    }
    
    public boolean isIntegral() {
        return true;
    }
    
    
    public BigRational add(BigRational b) {
        return b;
    }
    
    public BigRational subtract(BigRational b) {
        return b.negate();
    }
    
    public BigRational multiply(BigRational b) {
        return this;
    }
    
    @Override
    public BigRational square() {
        return this;
    }

    @Override
    public BigRational abs() {
        return this;
    }

    public BigRational divide(BigRational b) {
        return this;
    }
    
    public BigRational negate() {
        return this;
    }
    
    public BigRational reciprocal() {
        throw new ArithmeticException("reciprocal of 0");
    }
    
    public int signum() {
        return 0;
    }
    
    public int compareTo(BigRational b) {
        return BigInteger.ZERO.compareTo(b.getNumerator());
    }

    @Override
    protected long getDenominatorFixed() {
        return 1;
    }

    @Override
    protected long getNumeratorFixed() {
        return 0;
    }
    
}
