package io.examples.calculation.domain;

import io.vlingo.common.Completes;
import io.vlingo.lattice.model.object.ObjectEntity;
import io.vlingo.lattice.model.stateful.StatefulEntity;

import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

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
public class CalculationEntity extends ObjectEntity<CalculationState> implements Calculation {

    private CalculationState state;

    public CalculationEntity(final CalculationId id) {
        super(id.value);
        state = CalculationState.from(id);
    }

    @Override
    public Completes<CalculationState> calculate(final Operation operation,
                                                 final Integer anOperand,
                                                 final Integer anotherOperand,
                                                 final Set<CalculationState> existingCalculations) {
        final Optional<CalculationState> similar =
                findSimilarCalculation(operation, anOperand, anotherOperand, existingCalculations);

        if(similar.isPresent()) {
            return this.answerFrom(Completes.withSuccess(similar.get()));
        }

        return apply(state.calculate(operation, anOperand, anotherOperand), () -> state);
    }

    @Override
    protected CalculationState stateObject() {
        return state;
    }

    @Override
    protected void stateObject(final CalculationState calculationState) {
        this.state = calculationState;
    }

    @Override
    protected Class<CalculationState> stateObjectType() {
        return CalculationState.class;
    }

    private Optional<CalculationState> findSimilarCalculation(final Operation operation,
                                                              final Integer anOperand,
                                                              final Integer anotherOperand,
                                                              final Set<CalculationState> existingCalculations) {
        final Predicate<CalculationState> applicabilityFilter =
                state -> state.isApplicable(operation, anOperand, anotherOperand);

        return existingCalculations.stream().filter(applicabilityFilter).findFirst();
    }
}