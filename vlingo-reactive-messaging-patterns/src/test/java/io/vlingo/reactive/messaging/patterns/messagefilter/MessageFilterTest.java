// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.messagefilter;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;
import org.junit.Test;

import java.util.Arrays;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class MessageFilterTest {

    @Test
    public void testThatMessageFilterRuns() {

        System.out.println("Message Filter: is starting.");

        final World world = World.startWithDefaults("message-filter-test");

        final TestUntil until = TestUntil.happenings(3);

        final InventorySystem restrictedInventorySystemActor =
                world.actorFor(
                        Definition.has(
                                RestrictedInventorySystemActor.class,
                                Definition.parameters(until)),
                        InventorySystem.class);

        final InventorySystem notRestrictedInventorySystemActor =
                world.actorFor(
                        Definition.has(NotRestrictedInventorySystemActor.class,
                                Definition.parameters(until)),
                        InventorySystem.class);

        final InventorySystem inventoryMessageFilter =
                world.actorFor(
                        Definition.has(InventorySystemMessageFilter.class,
                                Definition.parameters(until, notRestrictedInventorySystemActor)),
                        InventorySystem.class);

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

        until.completes();

        System.out.println("Message Filter: is completed.");
    }
}