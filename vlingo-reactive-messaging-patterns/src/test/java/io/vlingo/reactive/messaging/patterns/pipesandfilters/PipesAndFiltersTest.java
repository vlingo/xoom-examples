// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.pipesandfilters;

import org.junit.Test;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;

public class PipesAndFiltersTest {
  @Test
  public void testThatPipesAndFiltersRuns() {
    System.out.println("PipesAndFilters: is starting.");

    final World world = World.startWithDefaults("pipesandfilters-test");

    final TestUntil until = TestUntil.happenings(9);

    final String orderText = "(encryption)(certificate)<order id='123'>...</order>";
    final byte[] rawOrderBytes = orderText.getBytes();

    final OrderProcessor filter5 = world.actorFor(OrderProcessor.class, OrderManagementSystem.class, until);
    final OrderProcessor filter4 = world.actorFor(OrderProcessor.class, Deduplicator.class, filter5, until);
    final OrderProcessor filter3 = world.actorFor(OrderProcessor.class, Authenticator.class, filter4, until);
    final OrderProcessor filter2 = world.actorFor(OrderProcessor.class, Decrypter.class, filter3, until);
    final OrderProcessor filter1 = world.actorFor(OrderProcessor.class, OrderAcceptanceEndpoint.class, filter2, until);

    filter1.processIncomingOrder(rawOrderBytes);
    filter1.processIncomingOrder(rawOrderBytes);

    until.completes();

    System.out.println("PipesAndFilters: is completed.");
  }
}
