package io.vlingo.reactive.messaging.patterns.throttler;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;

public class PrintingConsumer extends Actor implements Consumer {
    private final long startedAt;
    private final TestUntil until;

    public PrintingConsumer(TestUntil until) {
        this.until = until;
        this.startedAt = System.currentTimeMillis();
    }

    @Override
    public void onReceiveMessage(String message) {
        final long messageTimestamp = System.currentTimeMillis();
        logger().log(String.format("%d\t\t%s", messageTimestamp - startedAt, message));
        until.happened();
    }
}
