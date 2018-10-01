// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.aggregator;

import java.util.List;

import io.vlingo.reactive.messaging.patterns.aggregator.PriceQuotes.PriceQuote;

public interface PriceQuotesFulfillmentWatcher {
  void quotationFulfillmentCompleted(final QuotationFulfillment fulfillment);

  public final class QuotationFulfillment {
    public final String rfqId;
    public final int quotesRequested;
    public final List<PriceQuote> priceQuotes;

    public QuotationFulfillment(final String rfqId, final int quotesRequested, final List<PriceQuote> priceQuotes) {
      this.rfqId = rfqId;
      this.quotesRequested = quotesRequested;
      this.priceQuotes = priceQuotes;
    }

    @Override
    public String toString() {
      return "QuotationFulfillment[rfqId=" + rfqId + " quotesRequested=" + quotesRequested + " priceQuotes=" + priceQuotes + "]";
    }
  }
}
