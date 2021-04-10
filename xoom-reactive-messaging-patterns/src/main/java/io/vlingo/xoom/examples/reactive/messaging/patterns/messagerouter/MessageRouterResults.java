// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.messagerouter;

import io.vlingo.xoom.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageRouterResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterMessageProcessedByFirstProcessorCount = new AtomicInteger(0);
    public AtomicInteger afterMessageProcessedBySecondProcessorCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterMessageProcessedByFirstProcessorCount", (Integer increment) -> afterMessageProcessedByFirstProcessorCount.set(afterMessageProcessedByFirstProcessorCount.get() + increment))
                        .readingWith("afterMessageProcessedByFirstProcessorCount", () -> afterMessageProcessedByFirstProcessorCount.get())

                        .writingWith("afterMessageProcessedBySecondProcessorCount", (Integer increment) -> afterMessageProcessedBySecondProcessorCount.set(afterMessageProcessedBySecondProcessorCount.get() + increment))
                        .readingWith("afterMessageProcessedBySecondProcessorCount", () -> afterMessageProcessedBySecondProcessorCount.get());

        return access;
    }
}
