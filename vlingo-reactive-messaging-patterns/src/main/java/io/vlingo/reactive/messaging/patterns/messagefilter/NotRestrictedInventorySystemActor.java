// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.messagefilter;

import io.vlingo.actors.Actor;

public class NotRestrictedInventorySystemActor extends Actor implements InventorySystem {

    private final MessageFilterResults results;

    public NotRestrictedInventorySystemActor(final MessageFilterResults results) {
        this.results = results;
    }

    @Override
    public void processOrder(final Order order) {
        logger().debug("Handling " + order);
        results.access.writeUsing("afterOrderProcessedCount", 1);
    }

}