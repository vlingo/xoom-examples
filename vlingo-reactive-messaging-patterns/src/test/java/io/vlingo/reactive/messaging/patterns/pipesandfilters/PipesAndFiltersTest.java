// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.pipesandfilters;

import org.junit.Assert;
import org.junit.Test;

import io.vlingo.actors.World;
import io.vlingo.actors.testkit.AccessSafely;

public class PipesAndFiltersTest {
  @Test
  public void testThatPipesAndFiltersRuns() {
    System.out.println("PipesAndFilters: is starting.");

    final World world = World.startWithDefaults("pipesandfilters-test");

    final PipeAndFilterResults results = new PipeAndFilterResults();
    final AccessSafely access = results.afterCompleting(9);

    final String orderText = "(encryption)(certificate)<order id='123'>...</order>";
    final byte[] rawOrderBytes = orderText.getBytes();

    final OrderProcessor filter5 = world.actorFor(OrderProcessor.class, OrderManagementSystem.class, results);
    final OrderProcessor filter4 = world.actorFor(OrderProcessor.class, Deduplicator.class, filter5, results);
    final OrderProcessor filter3 = world.actorFor(OrderProcessor.class, Authenticator.class, filter4, results);
    final OrderProcessor filter2 = world.actorFor(OrderProcessor.class, Decrypter.class, filter3, results);
    final OrderProcessor filter1 = world.actorFor(OrderProcessor.class, OrderAcceptanceEndpoint.class, filter2, results);

    filter1.processIncomingOrder(rawOrderBytes);
    filter1.processIncomingOrder(rawOrderBytes);

    Assert.assertEquals(2, (int) access.readFrom("afterOrderAuthenticatedCount"));
    Assert.assertEquals(2, (int) access.readFrom("afterOrderDecryptedCount"));
    Assert.assertEquals(2, (int) access.readFrom("afterOrderDeduplicatedCount"));
    Assert.assertEquals(2, (int) access.readFrom("afterOrderAcceptedCount"));
    Assert.assertEquals(1, (int) access.readFrom("afterOrderManagedCount"));

    System.out.println("PipesAndFilters: is completed.");
  }
}
