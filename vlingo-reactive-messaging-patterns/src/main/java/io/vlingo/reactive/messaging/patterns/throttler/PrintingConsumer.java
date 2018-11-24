package io.vlingo.reactive.messaging.patterns.throttler;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Definition;
import io.vlingo.actors.World;

import static io.vlingo.actors.Definition.NoParameters;
import static io.vlingo.actors.Definition.has;
import static io.vlingo.actors.Definition.parameters;

public class PrintingConsumer extends Actor implements Consumer {
    private final long startedAt;

    public PrintingConsumer() {
        this.startedAt = System.currentTimeMillis();
    }

    @Override
    public void onReceiveMessage(String message) {
        final long messageTimestamp = System.currentTimeMillis();
        logger().log(String.format("%d\t\t%s", messageTimestamp, message));
    }

    public static void main(String[] args) {
        World world = World.startWithDefaults("lol");

        Producer delegate = world.actorFor(has(RandomStringProducer.class, NoParameters), Producer.class);
        Producer throttled = world.actorFor(has(ThrottledProducer.class, parameters(5, 1000, delegate)), Producer.class);
        Consumer consumer = world.actorFor(has(PrintingConsumer.class, NoParameters), Consumer.class);

        for (int i = 0; i < 10; i++) {
            throttled.produceMessage(consumer);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        world.terminate();
    }
}
