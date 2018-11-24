package io.vlingo.reactive.messaging.patterns.throttler;

public interface Producer {
    void produceMessage(final Consumer consumer);
}
