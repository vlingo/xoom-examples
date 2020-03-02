package io.examples.calculation.domain;

import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@MicronautTest
public class CalculationTests {

    @Test
    public void testAddition() {
        final Calculation calculation = Calculation.of(Operation.ADDITION, 2, 2);
        Assertions.assertEquals(4, calculation.result());
    }

    @Test
    public void testSubtraction() {
        final Calculation calculation = Calculation.of(Operation.SUBTRACTION, 3, 2);
        Assertions.assertEquals(1, calculation.result());
    }

    @Test
    public void testMultiplication() {
        final Calculation calculation = Calculation.of(Operation.MULTIPLICATION, 5, 5);
        Assertions.assertEquals(25, calculation.result());
    }
}
