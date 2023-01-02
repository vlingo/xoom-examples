// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.throttler;

import java.util.LinkedList;
import java.util.List;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.common.Cancellable;
import io.vlingo.xoom.common.Scheduled;

public class ThrottledProducer extends Actor implements Producer, Scheduled<Object> {
    private final List<Consumer> pending;
    private final int maxMessagesPerPeriod;
    private final Producer delegate;
    private int messagesSentInPeriod;
    private Cancellable periodRefresher;

    @SuppressWarnings("unchecked")
    public ThrottledProducer(final int maxMessagesPerPeriod, int period, final Producer delegate) {
        this.maxMessagesPerPeriod = maxMessagesPerPeriod;
        this.delegate = delegate;

        this.pending = new LinkedList<>();
        this.messagesSentInPeriod = 0;

        this.periodRefresher = scheduler().schedule(selfAs(Scheduled.class), null, 0, period);
    }

    @Override
    public void produceMessage(Consumer consumer) {
        if (shouldThrottle()) {
            pending.add(consumer);
        } else {
            doDispatchTo(consumer);
        }
    }

    @Override
    public void intervalSignal(Scheduled<Object> scheduled, Object data) {
      messagesSentInPeriod = 0;

      while (!shouldThrottle() && thereAreMessagesPending()) {
          final Consumer consumer = pending.get(0);
          doDispatchTo(consumer);
          pending.remove(0);
      }
    }

    @Override
    public void stop() {
      periodRefresher.cancel();

      super.stop();
    }

    private void doDispatchTo(Consumer consumer) {
        delegate.produceMessage(consumer);
        messagesSentInPeriod++;
    }

    private boolean thereAreMessagesPending() {
        return !pending.isEmpty();
    }

    private boolean shouldThrottle() {
        return messagesSentInPeriod >= maxMessagesPerPeriod;
    }
}
