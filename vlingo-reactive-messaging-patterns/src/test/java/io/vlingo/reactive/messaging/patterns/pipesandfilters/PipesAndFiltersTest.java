// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.pipesandfilters;

import org.junit.Test;

import io.vlingo.actors.Definition;
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

    final OrderProcessor filter5 = world.actorFor(Definition.has(OrderManagementSystem.class, Definition.parameters(until)), OrderProcessor.class);
    final OrderProcessor filter4 = world.actorFor(Definition.has(Deduplicator.class, Definition.parameters(filter5, until)), OrderProcessor.class);
    final OrderProcessor filter3 = world.actorFor(Definition.has(Authenticator.class, Definition.parameters(filter4, until)), OrderProcessor.class);
    final OrderProcessor filter2 = world.actorFor(Definition.has(Decrypter.class, Definition.parameters(filter3, until)), OrderProcessor.class);
    final OrderProcessor filter1 = world.actorFor(Definition.has(OrderAcceptanceEndpoint.class, Definition.parameters(filter2, until)), OrderProcessor.class);

    filter1.processIncomingOrder(rawOrderBytes);
    filter1.processIncomingOrder(rawOrderBytes);

    until.completes();

    System.out.println("PipesAndFilters: is completed.");
  }
}
