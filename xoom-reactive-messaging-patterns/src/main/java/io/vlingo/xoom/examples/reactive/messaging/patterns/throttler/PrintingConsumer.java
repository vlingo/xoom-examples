// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.throttler;

import io.vlingo.xoom.actors.Actor;

public class PrintingConsumer extends Actor implements Consumer {
    private final long startedAt;
    private final ThrottlerResults results;

    public PrintingConsumer(final ThrottlerResults results) {
        this.results = results;
        this.startedAt = System.currentTimeMillis();
    }

    @Override
    public void onReceiveMessage(String message) {
        final long messageTimestamp = System.currentTimeMillis();
        logger().debug(String.format("%d\t\t%s", messageTimestamp - startedAt, message));
        results.access.writeUsing("afterMessageReceivedCount", 1);
    }
}
