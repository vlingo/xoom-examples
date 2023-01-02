// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.pi.calculator;

import io.vlingo.xoom.actors.CompletesEventually;
import io.vlingo.xoom.actors.Stage;

final class CalculatorState {
  CompletesEventually completes;
  final PartitionCalculationInterest interest;
  double pi;
  int resultsCount;
  final int totalPartitionValues;
  final int totalMessages;
  int workerIndex;
  PartitionCalculator[] workers;

  CalculatorState(final Stage stage, final PartitionCalculationInterest interest, final int totalWorkers, final int totalPartitionValues, final int totalMessages) {
    this.totalPartitionValues = totalPartitionValues;
    this.totalMessages = totalMessages;
    this.pi = 0;
    this.resultsCount = 0;

    this.workerIndex = 0;
    this.workers = workers(stage, totalWorkers);
    
    this.interest = interest;
  }

  private PartitionCalculator[] workers(final Stage stage, final int totalWorkers) {
    final PartitionCalculator[] workers = new PartitionCalculator[totalWorkers];

    for (int idx = 0; idx < totalWorkers; ++idx) {
      workers[idx] = PartitionCalculator.on(stage);
    }

    return workers;
  }
}
