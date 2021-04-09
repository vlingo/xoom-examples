// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.throttler;

import io.vlingo.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class ThrottlerResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterMessageReceivedCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterMessageReceivedCount", (Integer increment) -> afterMessageReceivedCount.set(afterMessageReceivedCount.get() + increment))
                        .readingWith("afterMessageReceivedCount", () -> afterMessageReceivedCount.get());
        return access;
    }

}
