// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.aggregator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.examples.reactive.messaging.patterns.aggregator.PriceQuotes.PriceQuote;
import io.vlingo.xoom.examples.reactive.messaging.patterns.aggregator.PriceQuotesFulfillmentWatcher.QuotationFulfillment;

public class PriceQuoteAggregatorActor extends Actor implements PriceQuoteAggregator {
  private final Map<String,QuotationFulfillment> fulfillablePriceQuotes;
  private final PriceQuotesFulfillmentWatcher watcher;

  public PriceQuoteAggregatorActor(final PriceQuotesFulfillmentWatcher watcher) {
    this.watcher = watcher;
    this.fulfillablePriceQuotes = new HashMap<>();
  }

  @Override
  public void priceQuoteFulfilled(final PriceQuote priceQuote) {
    final QuotationFulfillment previousFulfillment = fulfillablePriceQuotes.get(priceQuote.rfqId);
    final List<PriceQuote> currentPriceQuotes = new ArrayList<>(previousFulfillment.priceQuotes);
    currentPriceQuotes.add(priceQuote);
    final QuotationFulfillment currentFulfillment =
      new QuotationFulfillment(
          previousFulfillment.rfqId,
          previousFulfillment.quotesRequested,
          currentPriceQuotes);

    if (currentPriceQuotes.size() >= currentFulfillment.quotesRequested) {
      watcher.quotationFulfillmentCompleted(currentFulfillment);
      fulfillablePriceQuotes.remove(priceQuote.rfqId);
    } else {
      fulfillablePriceQuotes.put(priceQuote.rfqId, currentFulfillment);
    }
  }

  @Override
  public void requiredQuotesFulfillment(final String rfqId, final int totalQuotesRequested) {
    fulfillablePriceQuotes.put(rfqId, new QuotationFulfillment(rfqId, totalQuotesRequested, new ArrayList<PriceQuote>()));
  }
}
