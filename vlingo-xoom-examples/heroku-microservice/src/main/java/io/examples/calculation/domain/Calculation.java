package io.examples.calculation.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code Calculation} has been modeled to serve a basic mathematical domain,
 * being able to calculate the following operations:
 *
 * <li>{@link Operation#ADDITION}</li>
 * <li>{@link Operation#SUBTRACTION}</li>
 * <li>{@link Operation#MULTIPLICATION}</li>
 *
 * @author Danilo Ambrosio
 */
public class Calculation {

    private final Integer result;
    private final Operation operation;
    private final List<Integer> operands = new ArrayList<>();

    public static Calculation of(final Operation operation,
                                 final Integer anOperand,
                                 final Integer anotherOperand) {
        return new Calculation(operation, anOperand, anotherOperand);
    }

    private Calculation(final Operation operation,
                        final Integer anOperand,
                        final Integer anotherOperand) {
        this.operation = operation;
        this.operands.addAll(Arrays.asList(anOperand, anotherOperand));
        this.result = operation.perform(this);
    }

    public boolean isApplicable(final Operation operation,
                                final Integer anOperand,
                                final Integer anotherOperand) {
        return this.operation.equals(operation) &&
                this.operands.containsAll(Arrays.asList(anOperand, anotherOperand));
    }

    protected Integer sum() {
        return this.operands.stream().reduce(0, Math::addExact);
    }

    protected Integer multiply() {
        return this.operands.stream().reduce(1, Math::multiplyExact);
    }

    protected Integer subtract() {
        return this.operands.stream().reduce(0, (a, b) -> Math.abs(a - b));
    }

    public Integer result() {
        return result;
    }
}