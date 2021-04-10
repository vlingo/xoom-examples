package io.vlingo.xoom.examples.reactive.messaging.patterns.messagefilter;

import io.vlingo.xoom.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageFilterResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterOrderProcessedCount = new AtomicInteger(0);
    public AtomicInteger afterOrderFilteredCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterOrderProcessedCount", (Integer increment) -> afterOrderProcessedCount.set(afterOrderProcessedCount.get() + increment))
                        .readingWith("afterOrderProcessedCount", () -> afterOrderProcessedCount.get())

                        .writingWith("afterOrderFilteredCount", (Integer increment) -> afterOrderFilteredCount.set(afterOrderFilteredCount.get() + increment))
                        .readingWith("afterOrderFilteredCount", () -> afterOrderFilteredCount.get());

        return access;
    }
}
