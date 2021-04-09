// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.pipesandfilters;

import io.vlingo.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class PipeAndFilterResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterOrderAuthenticatedCount = new AtomicInteger(0);
    public AtomicInteger afterOrderDecryptedCount = new AtomicInteger(0);
    public AtomicInteger afterOrderDeduplicatedCount = new AtomicInteger(0);
    public AtomicInteger afterOrderAcceptedCount = new AtomicInteger(0);
    public AtomicInteger afterOrderManagedCount = new AtomicInteger(0);


    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterOrderAuthenticatedCount", (Integer increment) -> afterOrderAuthenticatedCount.set(afterOrderAuthenticatedCount.get() + increment))
                        .readingWith("afterOrderAuthenticatedCount", () -> afterOrderAuthenticatedCount.get())

                        .writingWith("afterOrderDecryptedCount", (Integer increment) -> afterOrderDecryptedCount.set(afterOrderDecryptedCount.get() + increment))
                        .readingWith("afterOrderDecryptedCount", () -> afterOrderDecryptedCount.get())

                        .writingWith("afterOrderDeduplicatedCount", (Integer increment) -> afterOrderDeduplicatedCount.set(afterOrderDeduplicatedCount.get() + increment))
                        .readingWith("afterOrderDeduplicatedCount", () -> afterOrderDeduplicatedCount.get())

                        .writingWith("afterOrderAcceptedCount", (Integer increment) -> afterOrderAcceptedCount.set(afterOrderAcceptedCount.get() + increment))
                        .readingWith("afterOrderAcceptedCount", () -> afterOrderAcceptedCount.get())

                        .writingWith("afterOrderManagedCount", (Integer increment) -> afterOrderManagedCount.set(afterOrderManagedCount.get() + increment))
                        .readingWith("afterOrderManagedCount", () -> afterOrderManagedCount.get());





        return access;
    }
}
