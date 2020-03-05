package io.examples.calculation.repository;

import io.examples.calculation.domain.Calculation;
import io.examples.calculation.domain.Operation;

import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

/**
 * Once it is not intended to add any database on this application,
 * the {@code InMemoryCalculationRepository} is the basic and unique
 * implementation for {@link CalculationRepository} keeping the
 * {@link Calculation} data in memory.
 *
 * @author Danilo Ambrosio
 */
@Singleton
public class InMemoryCalculationRepository implements CalculationRepository {

    private static final Set<Calculation> CALCULATIONS = new HashSet<>();

    @Override
    public Calculation save(final Calculation calculation) {
        CALCULATIONS.add(calculation);
        return calculation;
    }

    @Override
    public Calculation applicableCalculationFor(final Operation operation,
                                                final Integer anOperand,
                                                final Integer anotherOperand) {
        return CALCULATIONS.stream()
                .filter(calculation -> calculation.isApplicable(operation, anOperand, anotherOperand))
                .findFirst()
                .orElse(null);
    }
}
