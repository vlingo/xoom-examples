// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.messagefilter;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.actors.testkit.AccessSafely;

public class MessageFilterTest {

    @Test
    public void testThatMessageFilterRuns() {

        System.out.println("Message Filter: is starting.");

        final World world = World.startWithDefaults("message-filter-test");

        final MessageFilterResults results = new MessageFilterResults();

        final AccessSafely access = results.afterCompleting(3);

        final InventorySystem restrictedInventorySystemActor =
                world.actorFor(InventorySystem.class, RestrictedInventorySystemActor.class, results);

        final InventorySystem notRestrictedInventorySystemActor =
                world.actorFor(InventorySystem.class, NotRestrictedInventorySystemActor.class, results);

        final InventorySystem inventoryMessageFilter =
                world.actorFor(InventorySystem.class, InventorySystemMessageFilter.class, results, notRestrictedInventorySystemActor);

        final Order order =
                new Order("1",
                        "TypeDEF",
                        Arrays.asList(
                                new OrderItem("2", "TypeDEF", "A description", 100d),
                                new OrderItem("3", "TypeDEF", "A description", 150d))
                                .stream().collect(toMap(OrderItem::orderItemId, identity())));

        restrictedInventorySystemActor.processOrder(order);
        notRestrictedInventorySystemActor.processOrder(order);
        inventoryMessageFilter.processOrder(order);

        Assert.assertEquals(1, (int) access.readFrom("afterOrderProcessedCount"));
        Assert.assertEquals(2, (int) access.readFrom("afterOrderFilteredCount"));

        System.out.println("Message Filter: is completed.");
    }
}