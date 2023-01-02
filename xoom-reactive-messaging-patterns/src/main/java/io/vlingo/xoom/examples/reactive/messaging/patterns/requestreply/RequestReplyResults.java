// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.requestreply;

import io.vlingo.xoom.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class RequestReplyResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterReplyReceivedCount = new AtomicInteger(0);
    public AtomicInteger afterQueryPerformedCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterReplyReceivedCount", (Integer increment) -> afterReplyReceivedCount.set(afterReplyReceivedCount.get() + increment))
                        .readingWith("afterReplyReceivedCount", () -> afterReplyReceivedCount.get())

                        .writingWith("afterQueryPerformedCount", (Integer increment) -> afterQueryPerformedCount.set(afterQueryPerformedCount.get() + increment))
                        .readingWith("afterQueryPerformedCount", () -> afterQueryPerformedCount.get());
        return access;
    }

}
