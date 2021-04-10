// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.competingconsumer;

import io.vlingo.xoom.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class CompetingConsumerResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterItemConsumedCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterItemConsumedCount", (Integer increment) -> afterItemConsumedCount.set(afterItemConsumedCount.get() + increment))
                        .readingWith("afterItemConsumedCount", () -> afterItemConsumedCount.get());

        return access;
    }
}
