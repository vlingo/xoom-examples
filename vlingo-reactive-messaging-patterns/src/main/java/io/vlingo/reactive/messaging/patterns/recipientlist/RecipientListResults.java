// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.recipientlist;

import io.vlingo.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class RecipientListResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterProcessorRegistered = new AtomicInteger(0);
    public AtomicInteger afterQuotationRemitted = new AtomicInteger(0);
    public AtomicInteger afterQuotationReceivedAtBudgetHikersCount = new AtomicInteger(0);
    public AtomicInteger afterQuotationReceivedAtHighSierraCount = new AtomicInteger(0);
    public AtomicInteger afterQuotationReceivedAtMountainAscentCount = new AtomicInteger(0);
    public AtomicInteger afterQuotationReceivedAtPinnacleGearCount = new AtomicInteger(0);
    public AtomicInteger afterQuotationReceivedAtRockBottomOuterwearCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterProcessorRegistered", (Integer increment) -> afterProcessorRegistered.set(afterProcessorRegistered.get() + increment))
                        .readingWith("afterProcessorRegistered", () -> afterProcessorRegistered.get())

                        .writingWith("afterQuotationRemitted", (Integer increment) -> afterQuotationRemitted.set(afterQuotationRemitted.get() + increment))
                        .readingWith("afterQuotationRemitted", () -> afterQuotationRemitted.get())

                        .writingWith("afterQuotationReceivedAtBudgetHikersCount", (Integer increment) -> afterQuotationReceivedAtBudgetHikersCount.set(afterQuotationReceivedAtBudgetHikersCount.get() + increment))
                        .readingWith("afterQuotationReceivedAtBudgetHikersCount", () -> afterQuotationReceivedAtBudgetHikersCount.get())

                        .writingWith("afterQuotationReceivedAtHighSierraCount", (Integer increment) -> afterQuotationReceivedAtHighSierraCount.set(afterQuotationReceivedAtHighSierraCount.get() + increment))
                        .readingWith("afterQuotationReceivedAtHighSierraCount", () -> afterQuotationReceivedAtHighSierraCount.get())

                        .writingWith("afterQuotationReceivedAtMountainAscentCount", (Integer increment) -> afterQuotationReceivedAtMountainAscentCount.set(afterQuotationReceivedAtMountainAscentCount.get() + increment))
                        .readingWith("afterQuotationReceivedAtMountainAscentCount", () -> afterQuotationReceivedAtMountainAscentCount.get())

                        .writingWith("afterQuotationReceivedAtPinnacleGearCount", (Integer increment) -> afterQuotationReceivedAtPinnacleGearCount.set(afterQuotationReceivedAtPinnacleGearCount.get() + increment))
                        .readingWith("afterQuotationReceivedAtPinnacleGearCount", () -> afterQuotationReceivedAtPinnacleGearCount.get())

                        .writingWith("afterQuotationReceivedAtRockBottomOuterwearCount", (Integer increment) -> afterQuotationReceivedAtRockBottomOuterwearCount.set(afterQuotationReceivedAtRockBottomOuterwearCount.get() + increment))
                        .readingWith("afterQuotationReceivedAtRockBottomOuterwearCount", () -> afterQuotationReceivedAtRockBottomOuterwearCount.get());

        return access;
    }

}
