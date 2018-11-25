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
