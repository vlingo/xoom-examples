// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.publishsubscribe;

import io.vlingo.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class MarketQuotationResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterQuotationReceivedAtGeneralSubscriberCount = new AtomicInteger(0);
    public AtomicInteger afterQuotationReceivedAtNASDAQSubscriberCount = new AtomicInteger(0);
    public AtomicInteger afterQuotationReceivedAtNYSESubscriberCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterQuotationReceivedAtGeneralSubscriberCount", (Integer increment) -> afterQuotationReceivedAtGeneralSubscriberCount.set(afterQuotationReceivedAtGeneralSubscriberCount.get() + increment))
                        .readingWith("afterQuotationReceivedAtGeneralSubscriberCount", () -> afterQuotationReceivedAtGeneralSubscriberCount.get())

                        .writingWith("afterQuotationReceivedAtNASDAQSubscriberCount", (Integer increment) -> afterQuotationReceivedAtNASDAQSubscriberCount.set(afterQuotationReceivedAtNASDAQSubscriberCount.get() + increment))
                        .readingWith("afterQuotationReceivedAtNASDAQSubscriberCount", () -> afterQuotationReceivedAtNASDAQSubscriberCount.get())

                        .writingWith("afterQuotationReceivedAtNYSESubscriberCount", (Integer increment) -> afterQuotationReceivedAtNYSESubscriberCount.set(afterQuotationReceivedAtNYSESubscriberCount.get() + increment))
                        .readingWith("afterQuotationReceivedAtNYSESubscriberCount", () -> afterQuotationReceivedAtNYSESubscriberCount.get());

        return access;
    }
}
