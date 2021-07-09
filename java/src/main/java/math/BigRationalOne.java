package math;

import java.math.BigInteger;

class BigRationalOne extends BigRational {

    public boolean isFixed() {
        return true;
    }
    
    @Override
    public BigRational add(BigRational b) {
        BigInteger b_den = b.getDenominator();
        BigInteger numerator = b_den.add(b.getNumerator());
        return valueOf(numerator, b_den);
    }

    @Override
    public int compareTo(BigRational b) {
        return b.getDenominator().compareTo(b.getNumerator());
    }

    @Override
    public BigRational divide(BigRational b) {
        return b.reciprocal();
    }

    @Override
    protected BigInteger getDenominator() {
        return BigInteger.ONE;
    }

    @Override
    protected BigInteger getNumerator() {
        return BigInteger.ONE;
    }

    @Override
    protected boolean isIntegral() {
        return true;
    }

    @Override
    public BigRational multiply(BigRational b) {
        return b;
    }
    

    @Override
    public BigRational square() {
        return this;
    }

    @Override
    public BigRational negate() {
        return BigRational.valueOf(-1, 1);
    }

    @Override
    public BigRational reciprocal() {
        return this;
    }

    @Override
    public BigRational abs() { return this; }

    @Override
    public int signum() {
        return 1;
    }

    @Override
    public BigRational subtract(BigRational b) {
        BigInteger b_den = b.getDenominator();
        BigInteger numerator = b_den.subtract(b.getNumerator());
        return valueOf(numerator, b_den);
    }

    @Override
    protected long getDenominatorFixed() {
        return 1;
    }

    @Override
    protected long getNumeratorFixed() {
        return 1;
    }

}
