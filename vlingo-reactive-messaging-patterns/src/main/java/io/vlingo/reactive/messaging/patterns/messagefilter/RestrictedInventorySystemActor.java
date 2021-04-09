// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.messagefilter;

import io.vlingo.actors.Actor;

public class RestrictedInventorySystemActor extends Actor implements InventorySystem {

    private final MessageFilterResults results;

    public RestrictedInventorySystemActor(final MessageFilterResults results) {
        this.results = results;
    }

    @Override
    public void processOrder(final Order order) {
        if(order.isType("TypeABC")) {
            logger().debug("Handling " + order);
            results.access.writeUsing("afterOrderProcessedCount", 1);
        } else {
            logger().debug("Filtering out " + order);
            results.access.writeUsing("afterOrderFilteredCount", 1);
        }
    }

}