// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.splitter;

import io.vlingo.xoom.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class SplitterResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterOrderPlacedCount = new AtomicInteger(0);
    public AtomicInteger afterOrderByReceivedAProcessorCount = new AtomicInteger(0);
    public AtomicInteger afterOrderByReceivedBProcessorCount = new AtomicInteger(0);
    public AtomicInteger afterOrderByReceivedCProcessorCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterOrderPlacedCount", (Integer increment) -> afterOrderPlacedCount.set(afterOrderPlacedCount.get() + increment))
                        .readingWith("afterOrderPlacedCount", () -> afterOrderPlacedCount.get())

                        .writingWith("afterOrderByReceivedAProcessorCount", (Integer increment) -> afterOrderByReceivedAProcessorCount.set(afterOrderByReceivedAProcessorCount.get() + increment))
                        .readingWith("afterOrderByReceivedAProcessorCount", () -> afterOrderByReceivedAProcessorCount.get())

                        .writingWith("afterOrderByReceivedBProcessorCount", (Integer increment) -> afterOrderByReceivedBProcessorCount.set(afterOrderByReceivedBProcessorCount.get() + increment))
                        .readingWith("afterOrderByReceivedBProcessorCount", () -> afterOrderByReceivedBProcessorCount.get())

                        .writingWith("afterOrderByReceivedCProcessorCount", (Integer increment) -> afterOrderByReceivedCProcessorCount.set(afterOrderByReceivedCProcessorCount.get() + increment))
                        .readingWith("afterOrderByReceivedCProcessorCount", () -> afterOrderByReceivedCProcessorCount.get());

        return access;
    }

}
