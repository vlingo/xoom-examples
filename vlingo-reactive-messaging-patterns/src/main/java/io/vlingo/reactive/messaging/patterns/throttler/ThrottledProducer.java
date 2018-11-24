package io.vlingo.reactive.messaging.patterns.throttler;

import io.vlingo.actors.Actor;
import io.vlingo.common.Cancellable;
import io.vlingo.common.Scheduled;

import java.util.LinkedList;
import java.util.List;

public class ThrottledProducer extends Actor implements Producer {
    private final List<Consumer> pending;
    private final int maxMessagesPerPeriod;
    private final Producer delegate;
    private final int period;
    private int messagesSentInPeriod;
    private Cancellable periodRefresher;

    public ThrottledProducer(final int maxMessagesPerPeriod, int period, final Producer delegate) {
        this.maxMessagesPerPeriod = maxMessagesPerPeriod;
        this.delegate = delegate;
        this.period = period;

        this.pending = new LinkedList<>();
        this.messagesSentInPeriod = 0;

        this.periodRefresher = scheduler().schedule(this::refreshPeriod, null, 0, period);
    }

    @Override
    public void produceMessage(Consumer consumer) {
        if (shouldThrottle()) {
            pending.add(consumer);
        } else {
            delegate.produceMessage(consumer);
            messagesSentInPeriod++;
        }
    }

    private void refreshPeriod(Scheduled scheduled, Object data) {
        messagesSentInPeriod = 0;

        while (!shouldThrottle() && thereAreMessagesPending()) {
            dispatchMessage();
        }
    }

    private void dispatchMessage() {
        final Consumer consumer = pending.get(0);
        delegate.produceMessage(consumer);

        messagesSentInPeriod++;
        pending.remove(0);
    }

    private boolean thereAreMessagesPending() {
        return !pending.isEmpty();
    }

    private boolean shouldThrottle() {
        return messagesSentInPeriod >= maxMessagesPerPeriod;
    }
}
