// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.contentbasedrouter.actor;

import io.vlingo.xoom.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class OrderRoutingResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterOrderRoutedCount = new AtomicInteger(0);
    public AtomicInteger afterStoppedCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterOrderRoutedCount", (Integer increment) -> afterOrderRoutedCount.set(afterOrderRoutedCount.get() + increment))
                        .readingWith("afterOrderRoutedCount", () -> afterOrderRoutedCount.get())

                        .writingWith("afterStoppedCount", (Integer increment) -> afterStoppedCount.set(afterStoppedCount.get() + increment))
                        .readingWith("afterStoppedCount", () -> afterStoppedCount.get());

        return access;
    }

}
