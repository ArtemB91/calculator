package org.bychkov;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalculatorTest {

    private static final Calculator calc = new Calculator();

    @Test
    void expressions_calculate() {
        Assertions.assertEquals(7D, calc.calculate("-5+(-6*(-2))"));
        Assertions.assertEquals(-17D, calc.calculate("-5+(-6*(-2+4))"));
        Assertions.assertEquals(-98D, calc.calculate("100-200+((5-1)/2)"));
        Assertions.assertEquals(6D, calc.calculate("(100+20)/(10*2)"));
        Assertions.assertEquals(6D, calc.calculate("((100+20)/(10*2))"));

        Assertions.assertEquals(-100D, calc.calculate("-100"));
        Assertions.assertEquals(100D, calc.calculate("100"));
        Assertions.assertEquals(100D, calc.calculate("(100)"));
        Assertions.assertEquals(-100D, calc.calculate("(((((-100)))))"));
    }

    @Test
    void invalidExpressions_calculate_throws() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> calc.calculate("(1+2"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> calc.calculate("(1+2))"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> calc.calculate("1/0"));
    }
}