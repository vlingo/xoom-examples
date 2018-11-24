package io.vlingo.reactive.messaging.patterns.throttler;

import io.vlingo.actors.Actor;

import java.util.UUID;

public class RandomStringProducer extends Actor implements Producer {
    @Override
    public void produceMessage(final Consumer consumer) {
        consumer.onReceiveMessage(UUID.randomUUID().toString());
    }
}
