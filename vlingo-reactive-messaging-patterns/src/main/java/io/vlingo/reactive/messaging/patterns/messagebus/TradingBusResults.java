// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.messagebus;

import io.vlingo.actors.testkit.AccessSafely;

import java.util.concurrent.atomic.AtomicInteger;

public class TradingBusResults {

    public AccessSafely access = afterCompleting(0);

    public AtomicInteger afterCommandDispatchedCount = new AtomicInteger(0);
    public AtomicInteger afterNotificationDispatchedCount = new AtomicInteger(0);
    public AtomicInteger afterHandlerRegisteredCount = new AtomicInteger(0);
    public AtomicInteger afterInterestRegisteredCount = new AtomicInteger(0);
    public AtomicInteger afterMarketAnalysisBuyOrderExecutedCount = new AtomicInteger(0);
    public AtomicInteger afterMarketAnalysisSellOrderExecutedCount = new AtomicInteger(0);
    public AtomicInteger afterPortfolioManagerBuyOrderExecutedCount = new AtomicInteger(0);
    public AtomicInteger afterPortfolioManagerSellOrderExecutedCount = new AtomicInteger(0);
    public AtomicInteger afterStockTraderBuyOrderExecutedCount = new AtomicInteger(0);
    public AtomicInteger afterStockTraderSellOrderExecutedCount = new AtomicInteger(0);

    public AccessSafely afterCompleting(final int times) {
        access =
                AccessSafely.afterCompleting(times)
                        .writingWith("afterCommandDispatchedCount", (Integer increment) -> afterCommandDispatchedCount.set(afterCommandDispatchedCount.get() + increment))
                        .readingWith("afterCommandDispatchedCount", () -> afterCommandDispatchedCount.get())

                        .writingWith("afterNotificationDispatchedCount", (Integer increment) -> afterNotificationDispatchedCount.set(afterNotificationDispatchedCount.get() + increment))
                        .readingWith("afterNotificationDispatchedCount", () -> afterNotificationDispatchedCount.get())

                        .writingWith("afterHandlerRegisteredCount", (Integer increment) -> afterHandlerRegisteredCount.set(afterHandlerRegisteredCount.get() + increment))
                        .readingWith("afterHandlerRegisteredCount", () -> afterHandlerRegisteredCount.get())

                        .writingWith("afterInterestRegisteredCount", (Integer increment) -> afterInterestRegisteredCount.set(afterInterestRegisteredCount.get() + increment))
                        .readingWith("afterInterestRegisteredCount", () -> afterInterestRegisteredCount.get())

                        .writingWith("afterMarketAnalysisBuyOrderExecutedCount", (Integer increment) -> afterMarketAnalysisBuyOrderExecutedCount.set(afterMarketAnalysisBuyOrderExecutedCount.get() + increment))
                        .readingWith("afterMarketAnalysisBuyOrderExecutedCount", () -> afterMarketAnalysisBuyOrderExecutedCount.get())

                        .writingWith("afterMarketAnalysisSellOrderExecutedCount", (Integer increment) -> afterMarketAnalysisSellOrderExecutedCount.set(afterMarketAnalysisSellOrderExecutedCount.get() + increment))
                        .readingWith("afterMarketAnalysisSellOrderExecutedCount", () -> afterMarketAnalysisSellOrderExecutedCount.get())

                        .writingWith("afterPortfolioManagerBuyOrderExecutedCount", (Integer increment) -> afterPortfolioManagerBuyOrderExecutedCount.set(afterPortfolioManagerBuyOrderExecutedCount.get() + increment))
                        .readingWith("afterPortfolioManagerBuyOrderExecutedCount", () -> afterPortfolioManagerBuyOrderExecutedCount.get())

                        .writingWith("afterPortfolioManagerSellOrderExecutedCount", (Integer increment) -> afterPortfolioManagerSellOrderExecutedCount.set(afterPortfolioManagerSellOrderExecutedCount.get() + increment))
                        .readingWith("afterPortfolioManagerSellOrderExecutedCount", () -> afterPortfolioManagerSellOrderExecutedCount.get())

                        .writingWith("afterStockTraderBuyOrderExecutedCount", (Integer increment) -> afterStockTraderBuyOrderExecutedCount.set(afterStockTraderBuyOrderExecutedCount.get() + increment))
                        .readingWith("afterStockTraderBuyOrderExecutedCount", () -> afterStockTraderBuyOrderExecutedCount.get())

                        .writingWith("afterStockTraderSellOrderExecutedCount", (Integer increment) -> afterStockTraderSellOrderExecutedCount.set(afterStockTraderSellOrderExecutedCount.get() + increment))
                        .readingWith("afterStockTraderSellOrderExecutedCount", () -> afterStockTraderSellOrderExecutedCount.get());

        return access;
    }
}
