// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.examples.calculation.domain;

import io.examples.infrastructure.CalculationQueryProvider;
import io.vlingo.actors.Address;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;

import java.util.Set;
import java.util.UUID;

public interface Calculation {

    static String generateName() {
        return "O:" + UUID.randomUUID().toString();
    }

    static Completes<CalculationState> calculate(final Stage stage,
                                                 final Operation operation,
                                                 final Integer anOperand,
                                                 final Integer anotherOperand) {

        final Completes<Set<CalculationState>> calculations =
                CalculationQueryProvider.instance().queries().allCalculations();

        return calculations.andThenTo(existingCalculations -> {
            final Address address =
                    stage.addressFactory().uniqueWith(generateName());

            final Definition definition =
                    Definition.has(CalculationEntity.class, Definition.parameters(CalculationId.from(address.idString())));

            final Calculation calculation = stage.actorFor(Calculation.class, definition, address);

            return calculation.calculate(operation, anOperand, anotherOperand, existingCalculations);
        });
    }

    Completes<CalculationState> calculate(final Operation operation, final Integer anOperand, final Integer anotherOperand, final Set<CalculationState> existingCalculations);
}
