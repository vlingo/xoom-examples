// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.pi.calculator;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.Stoppable;
import io.vlingo.xoom.common.Completes;

public class CalculatorActor extends Actor implements Calculator, PartitionCalculationInterest {
  private final CalculatorState state;

  public CalculatorActor(final int totalWorkers, final int totalPartitionValues, final int totalMessages) {
    this.state = new CalculatorState(stage(), selfAs(PartitionCalculationInterest.class), totalWorkers, totalPartitionValues, totalMessages);
  }

  @Override
  public Completes<Double> calculate() {
    state.completes = completesEventually();
    for (int start = 0; start < state.totalMessages; ++start) {
      calculatePartition(start);
    }
    return completes();
  }

  @Override
  public void informCalculation(final double result) {
    state.pi += result;

    if (++state.resultsCount >= state.totalMessages) {
      state.completes.with(state.pi);

      selfAs(Stoppable.class).stop();
    }
  }

  private void calculatePartition(final int start) {
    state.workers[state.workerIndex++].calculate(start, state.totalPartitionValues, state.interest);

    if (state.workerIndex >= state.workers.length) {
      state.workerIndex = 0;
    }
  }
}
