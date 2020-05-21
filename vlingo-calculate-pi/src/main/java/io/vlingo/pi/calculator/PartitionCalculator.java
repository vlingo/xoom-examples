// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.pi.calculator;

import io.vlingo.actors.Stage;

public interface PartitionCalculator {
  static PartitionCalculator on(final Stage stage) {
    return stage.actorFor(PartitionCalculator.class, PartitionCalculatorActor.class);
  }

  void calculate(final int start, final int totalPartitionValues, final PartitionCalculationInterest interest);
}
