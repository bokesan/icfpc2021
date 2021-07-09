package math;

import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class BigRationalTest {

    @Test
    public void add() throws Exception {
        BigRational x = BigRational.valueOf(3,2);
        BigRational y = BigRational.valueOf(4,3);
        assertEquals(BigRational.valueOf(17,6), x.add(y));
        assertEquals(BigRational.valueOf(17,6), y.add(x));
    }

    @Test
    public void subtract() throws Exception {
        BigRational x = BigRational.valueOf(3,2);
        BigRational y = BigRational.valueOf(4,3);
        assertTrue(x.subtract(x).isZero());
        assertEquals(BigRational.valueOf(1,6), x.subtract(y));
        assertEquals(BigRational.valueOf(-1,6), y.subtract(x));
    }

    @Test
    public void multiply() throws Exception {
        BigRational x = BigRational.valueOf(3,2);
        BigRational y = BigRational.valueOf(4,3);
        assertEquals(BigRational.valueOf(2), x.multiply(y));
        assertEquals(BigRational.valueOf(2), y.multiply(x));
    }

    @Test
    public void multiplyNegative() throws Exception {
        BigRational x = BigRational.valueOf(3,2);
        BigRational y = BigRational.valueOf(-4,3);
        assertEquals(BigRational.valueOf(-2), x.multiply(y));
        assertEquals(BigRational.valueOf(-2), y.multiply(x));
    }

    @Test
    public void multiplyNegNeg() throws Exception {
        BigRational x = BigRational.valueOf(3,-2);
        BigRational y = BigRational.valueOf(-4,3);
        assertEquals(BigRational.valueOf(2), x.multiply(y));
        assertEquals(BigRational.valueOf(2), y.multiply(x));
    }

    @Test
    public void multiplyZero() throws Exception {
        BigRational x = BigRational.valueOf(3,2);
        assertTrue(x.multiply(BigRational.ZERO).isZero());
        assertTrue(BigRational.ZERO.multiply(x).isZero());
    }

    @Test
    public void divide() throws Exception {
        BigRational x = BigRational.valueOf(3,2);
        BigRational y = BigRational.valueOf(2);
        assertEquals(BigRational.valueOf(3,4), x.divide(y));
    }

    @Test
    public void divideNegative() throws Exception {
        BigRational x = BigRational.valueOf(3,2);
        BigRational y = BigRational.valueOf(-2);
        assertEquals(BigRational.valueOf(-3,4), x.divide(y));
    }

    @Test
    public void negate() throws Exception {
        BigRational x = BigRational.valueOf(3,-17);
        assertEquals(BigRational.valueOf(3,17), x.negate());
    }

    @Test
    public void reciprocal() throws Exception {
        BigRational x = BigRational.valueOf(4,18);
        assertEquals(BigRational.valueOf(18,4), x.reciprocal());
    }

    @Test
    public void testOne() throws Exception {
        BigRational x = BigRational.ONE;
        assertEquals(BigInteger.ONE, x.getNumerator());
        assertEquals(BigInteger.ONE, x.getDenominator());
        assertEquals(1, x.getNumeratorFixed());
        assertEquals(1, x.getDenominatorFixed());
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroDemonThrows() throws Exception {
        BigRational.valueOf(2,0);
    }

    @Test
    public void compareTo() throws Exception {
        BigRational x = BigRational.valueOf(2,3);
        BigRational x1 = BigRational.valueOf(2,3);
        BigRational y = BigRational.valueOf(4,5);
        assertEquals(0, x.compareTo(x1));
        assertTrue(x.compareTo(y) < 0);
        assertTrue(y.compareTo(x) > 0);
    }

    @Test
    public void compareToNeg() throws Exception {
        BigRational x = BigRational.valueOf(-2,3);
        BigRational x1 = BigRational.valueOf(-2,3);
        BigRational y = BigRational.valueOf(4,5);
        assertEquals(0, x.compareTo(x1));
        assertTrue(x.compareTo(y) < 0);
        assertTrue(y.compareTo(x) > 0);
    }

    @Test
    public void min() throws Exception {
        BigRational x = BigRational.valueOf(2,3);
        BigRational y = BigRational.valueOf(4,5);
        assertEquals(x, x.min(y));
        assertEquals(x, y.min(x));
    }

    @Test
    public void max() throws Exception {
        BigRational x = BigRational.valueOf(2,3);
        BigRational y = BigRational.valueOf(4,5);
        assertEquals(y, x.max(y));
        assertEquals(y, y.max(x));
    }

    @Test
    public void isZero() throws Exception {
        BigRational x = BigRational.ZERO;
        assertTrue(x.isZero());
    }

}