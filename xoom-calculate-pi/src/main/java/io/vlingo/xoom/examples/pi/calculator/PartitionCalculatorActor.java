// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.pi.calculator;

import io.vlingo.xoom.actors.Actor;

public class PartitionCalculatorActor extends Actor implements PartitionCalculator {
  public PartitionCalculatorActor() { }

  @Override
  public void calculate(final int start, final int totalPartitionValues, final PartitionCalculationInterest interest) {
    double accumulator = 0;

    final int limit = (start + 1) * totalPartitionValues - 1;

    for (int i = start * totalPartitionValues; i <= limit; i++) {
      accumulator += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1);
    }

    interest.informCalculation(accumulator);
  }
}
