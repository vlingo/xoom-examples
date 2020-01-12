// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.pointtopointchannel;

import io.vlingo.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class PointToPointResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterMessageProcessedCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterMessageProcessedCount", (Integer increment) -> afterMessageProcessedCount.set(afterMessageProcessedCount.get() + increment))
                        .readingWith("afterMessageProcessedCount", () -> afterMessageProcessedCount.get());

        return access;
    }
}
