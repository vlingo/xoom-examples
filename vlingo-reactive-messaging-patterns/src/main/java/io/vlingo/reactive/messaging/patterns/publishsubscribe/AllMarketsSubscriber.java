// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.publishsubscribe;

import io.vlingo.actors.Actor;
import io.vlingo.actors.pubsub.Publication;
import io.vlingo.actors.pubsub.Subscriber;
import io.vlingo.actors.testkit.TestUntil;

public class AllMarketsSubscriber extends Actor implements Subscriber<PriceQuoted> {

    private final TestUntil until;

    public AllMarketsSubscriber(final TestUntil until) {
        this.until = until;
    }

    @Override
    public void receive(final PriceQuoted priceQuoted) {
        logger().log("AllMarketsSubscriber received " + priceQuoted);
        until.happened();
    }
}
