// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.aggregator;

import io.vlingo.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class AggregatorResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterQuotationFulfillmentCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterQuotationFulfillmentCount", (Integer increment) -> afterQuotationFulfillmentCount.set(afterQuotationFulfillmentCount.get() + increment))
                        .readingWith("afterQuotationFulfillmentCount", () -> afterQuotationFulfillmentCount.get());

        return access;
    }
}
