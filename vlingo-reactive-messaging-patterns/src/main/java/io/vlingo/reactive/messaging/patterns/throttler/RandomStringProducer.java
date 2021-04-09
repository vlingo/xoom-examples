// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.throttler;

import io.vlingo.actors.Actor;


public class RandomStringProducer extends Actor implements Producer {
    private int messageCount;

    public RandomStringProducer() {
        this.messageCount = 0;
    }

    @Override
    public void produceMessage(final Consumer consumer) {
        consumer.onReceiveMessage(String.valueOf(++messageCount));
    }
}
