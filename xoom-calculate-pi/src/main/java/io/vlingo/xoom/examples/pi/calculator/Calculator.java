// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.pi.calculator;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.actors.Stoppable;
import io.vlingo.xoom.common.Completes;

public interface Calculator extends Stoppable {
  static Calculator with(final Stage stage, final int totalWorkers, final int totalPartitionValues, final int totalMessages) {
    return stage.actorFor(Calculator.class, CalculatorActor.class, totalWorkers, totalPartitionValues, totalMessages);
  }

  Completes<Double> calculate();
}
