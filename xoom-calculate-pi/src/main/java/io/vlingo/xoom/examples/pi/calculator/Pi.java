// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.pi.calculator;

import java.util.concurrent.CountDownLatch;

import io.vlingo.xoom.actors.World;

public class Pi {
  public static void main(final String[] args) throws Exception {
    final Pi pi = Pi.calculatorWith(4, 10_000, 10_000);

    pi.calculate();
  }

  private final Calculator calculator;

  private static Pi calculatorWith(final int totalWorkers, final int totalPartitionValues, final int totalMessages) {
    final World world = World.startWithDefaults("Pi");

    final Calculator calculator = Calculator.with(world.stage(), totalWorkers, totalPartitionValues, totalMessages);

    return new Pi(calculator);
  }

  private Pi(final Calculator calculator) {
    this.calculator = calculator;
  }

  private void calculate() throws Exception {
    final CountDownLatch latch = new CountDownLatch(1);
    final long startTime = System.currentTimeMillis();

    calculator.calculate().andFinallyConsume(pi -> {
      final long totalTime = System.currentTimeMillis() - startTime;

      System.out.println("Pi: " + pi + " Time: " + totalTime + " ms");

      latch.countDown();
    });

    latch.await();
    System.out.println("Done.");
    System.exit(0);
  }
}
