package io.examples.calculation.domain;

import java.util.function.Function;

/**
 * The {@code Operation} specifies the supported mathematical
 * operations on {@link Calculation}.
 *
 * @author Danilo Ambrosio
 */
public enum Operation {

    ADDITION(Calculation::sum),
    SUBTRACTION(Calculation::subtract),
    MULTIPLICATION(Calculation::multiply);

    private final Function<Calculation, Integer> function;

    Operation(final Function<Calculation, Integer> function) {
        this.function = function;
    }

    public static Operation withName(final String operationName) {
        return valueOf(operationName.toUpperCase());
    }

    public Integer perform(final Calculation calculation) {
        return this.function.apply(calculation);
    }
}
