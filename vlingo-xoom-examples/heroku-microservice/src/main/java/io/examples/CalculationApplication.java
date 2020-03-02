package io.examples;

import io.examples.calculation.domain.Calculation;
import io.micronaut.runtime.Micronaut;

/**
 * The {@code CalculationApplication} is a microservice that implements features in the {@link Calculation} context.
 */
public class CalculationApplication {

    public static void main(String[] args) {
        Micronaut.run(CalculationApplication.class);
    }
}