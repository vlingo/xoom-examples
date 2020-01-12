// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.messagefilter;

import io.vlingo.actors.Actor;

public class InventorySystemMessageFilter extends Actor implements InventorySystem {

    private final MessageFilterResults results;
    private final InventorySystem inventorySystem;

    public InventorySystemMessageFilter(final MessageFilterResults results, final InventorySystem inventorySystem) {
        this.inventorySystem = inventorySystem;
        this.results = results;
    }

    @Override
    public void processOrder(final Order order) {
        if(order.isType("TypeABC")) {
            inventorySystem.processOrder(order);
            results.access.writeUsing("afterOrderProcessedCount", 1);
        } else {
            logger().debug("Filtering out " + order);
            results.access.writeUsing("afterOrderFilteredCount", 1);
        }
    }
}
