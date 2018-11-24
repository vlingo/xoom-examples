package io.vlingo.reactive.messaging.patterns.throttler;

import io.vlingo.actors.Actor;

import java.util.UUID;

public class RandomStringProducer extends Actor implements Producer {
    @Override
    public void produceMessage(Consumer consumer) {
        final UUID uuid = UUID.randomUUID();
        consumer.onReceiveMessage(uuid.toString());
    }
}
