// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.messageexpiration;

import io.vlingo.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageExpirationResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterOrderExpiredCount = new AtomicInteger(0);
    public AtomicInteger afterOrderPlacedCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterOrderExpiredCount", (Integer increment) -> afterOrderExpiredCount.set(afterOrderExpiredCount.get() + increment))
                        .readingWith("afterOrderExpiredCount", () -> afterOrderExpiredCount.get())

                        .writingWith("afterOrderPlacedCount", (Integer increment) -> afterOrderPlacedCount.set(afterOrderPlacedCount.get() + increment))
                        .readingWith("afterOrderPlacedCount", () -> afterOrderPlacedCount.get());

        return access;
    }

}
