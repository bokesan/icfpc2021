package math;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class TrigTest {

    @Test
    public void sin30() throws Exception {
        assertEquals(BigRational.valueOf(1,2), Trig.sin(BigRational.valueOf(30)));
    }

    @Test
    public void sin45() {
        assertEquals(BigRational.SQRT2_CEIL.reciprocal(), Trig.sin(BigRational.valueOf(45)));
    }

    @Test
    public void cos60() throws Exception {
        assertEquals(BigRational.valueOf(1,2), Trig.cos(BigRational.valueOf(60)));
    }

    @Test
    public void tan45() throws Exception {
        assertEquals(BigRational.ONE, Trig.tan(BigRational.valueOf(45)));
    }

}