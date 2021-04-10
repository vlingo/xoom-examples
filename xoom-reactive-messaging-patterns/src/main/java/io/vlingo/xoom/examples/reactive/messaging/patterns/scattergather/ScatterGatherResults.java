// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.scattergather;

import io.vlingo.xoom.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class ScatterGatherResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterProcessorRegisteredCount = new AtomicInteger(0);
    public AtomicInteger afterBestPriceQuotationRegisteredCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterProcessorRegisteredCount", (Integer increment) -> afterProcessorRegisteredCount.set(afterProcessorRegisteredCount.get() + increment))
                        .readingWith("afterProcessorRegisteredCount", () -> afterProcessorRegisteredCount.get())

                        .writingWith("afterBestPriceQuotationRegisteredCount", (Integer increment) -> afterBestPriceQuotationRegisteredCount.set(afterBestPriceQuotationRegisteredCount.get() + increment))
                        .readingWith("afterBestPriceQuotationRegisteredCount", () -> afterBestPriceQuotationRegisteredCount.get());

        return access;
    }

}
