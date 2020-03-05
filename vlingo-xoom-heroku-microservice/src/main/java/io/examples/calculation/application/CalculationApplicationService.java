package io.examples.calculation.application;

import io.examples.calculation.domain.Calculation;
import io.examples.calculation.domain.Operation;
import io.examples.calculation.endpoint.CalculationEndpoint;
import io.examples.calculation.endpoint.v1.CalculationResource;
import io.examples.calculation.repository.CalculationRepository;
import io.vlingo.common.Completes;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;

/**
 * The {@code CalculationApplicationService} exposes operations and business logic that
 * pertains to the {@link Calculation} domain model. This service forms an anti-corruption
 * layer that is exposed to consumers using the {@link CalculationResource}.
 *
 * @author Danilo Ambrosio
 * @see CalculationEndpoint
 */
@Singleton
public class CalculationApplicationService {

    private final CalculationRepository calculationRepository;

    public CalculationApplicationService(final CalculationRepository calculationRepository) {
        this.calculationRepository = calculationRepository;
    }

    public Completes<Calculation> calculate(final ExecuteCalculation executeCalculation) {
        final Operation operation = Operation.withName(executeCalculation.operationName());

        final Calculation existingCalculation =
                calculationRepository.applicableCalculationFor(operation, executeCalculation.firstOperand(), executeCalculation.secondOperand());

        if(existingCalculation != null) {
            return Completes.withSuccess(existingCalculation);
        }

        return Completes.withSuccess(
                calculationRepository.save(
                    Calculation.of(operation, executeCalculation.firstOperand(), executeCalculation.secondOperand())
                ));
    }

    public Completes<List<Operation>> retrieveSupportedOperations() {
        return Completes.withSuccess(Arrays.asList(Operation.values()));
    }

}
