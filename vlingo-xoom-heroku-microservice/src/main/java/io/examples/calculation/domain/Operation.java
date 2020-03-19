package io.examples.calculation.domain;

import java.util.function.Function;

/**
 * The {@code Operation} specifies the supported mathematical
 * operations on {@link CalculationEntity}.
 *
 * @author Danilo Ambrosio
 */
public enum Operation {

    ADDITION(CalculationState::sum),
    SUBTRACTION(CalculationState::subtract),
    MULTIPLICATION(CalculationState::multiply);

    private final Function<CalculationState, Integer> function;

    Operation(final Function<CalculationState, Integer> function) {
        this.function = function;
    }

    public static Operation withName(final String operationName) {
        return valueOf(operationName.toUpperCase());
    }

    public Integer perform(final CalculationState calculationState) {
        return this.function.apply(calculationState);
    }
}
