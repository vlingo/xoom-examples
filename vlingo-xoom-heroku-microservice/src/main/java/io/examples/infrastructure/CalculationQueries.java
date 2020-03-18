// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.examples.infrastructure;

import io.examples.calculation.domain.CalculationState;
import io.examples.calculation.domain.Operation;
import io.vlingo.common.Completes;

import java.util.List;
import java.util.Optional;

public interface CalculationQueries {

    Completes<List<CalculationState>> allCalculations();

    Completes<Optional<CalculationState>> calculationOf(final Operation operation, final Integer anOperand, final Integer anotherOperand);

}
