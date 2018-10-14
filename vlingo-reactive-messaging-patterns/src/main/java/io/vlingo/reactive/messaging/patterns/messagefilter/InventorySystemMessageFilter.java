// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.messagefilter;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;

public class InventorySystemMessageFilter extends Actor implements InventorySystem {

    private final TestUntil until;
    private final InventorySystem inventorySystem;

    public InventorySystemMessageFilter(final TestUntil until, final InventorySystem inventorySystem) {
        this.inventorySystem = inventorySystem;
        this.until = until;
    }

    @Override
    public void orderPlaced(final OrderPlaced orderPlaced) {
        if(orderPlaced.isType("Type")) {
            inventorySystem.orderPlaced(orderPlaced);
        } else {
            System.out.println("Filtering out " + orderPlaced.order());
            until.happened();
        }
    }
}
