package io.examples.calculation.domain;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.stateful.StatefulEntity;

/**
 * The {@code Calculation} serves a basic mathematical domain,
 * being able to calculate the following operations:
 *
 * <li>{@link Operation#ADDITION}</li>
 * <li>{@link Operation#SUBTRACTION}</li>
 * <li>{@link Operation#MULTIPLICATION}</li>
 *
 * @author Danilo Ambrosio
 */
public class CalculationEntity extends StatefulEntity<CalculationState> implements Calculation {

    private CalculationState state;

    public CalculationEntity(final CalculationId id) {
        super(id.value);
        state = CalculationState.from(id);
    }

    @Override
    public Completes<CalculationState> calculate(final Operation operation,
                                                 final Integer anOperand,
                                                 final Integer anotherOperand) {
        return apply(state.calculate(operation, anOperand, anotherOperand), () -> state);
    }

    @Override
    protected void state(final CalculationState state) {
        this.state = state;
    }

    @Override
    protected Class<CalculationState> stateType() {
        return CalculationState.class;
    }
}