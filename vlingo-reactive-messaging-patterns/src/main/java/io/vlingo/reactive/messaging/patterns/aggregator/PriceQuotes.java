// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.aggregator;

public interface PriceQuotes {
  void requestPriceQuote(final PriceQuoteRequest request, final PriceQuoteRequester requester);

  public final class PriceQuoteRequest {
    public final String itemId;
    public final Double orderTotalRetailPrice;
    public final Double retailPrice;
    public final String rfqId;

    public PriceQuoteRequest(final String rfqId, final String itemId, final Double retailPrice, final Double orderTotalRetailPrice) {
      this.rfqId = rfqId;
      this.itemId = itemId;
      this.retailPrice = retailPrice;
      this.orderTotalRetailPrice = orderTotalRetailPrice;
    }
  }

  public final class PriceQuote {
    public final String quoterId;
    public final String itemId;
    public final Double price;
    public final Double retailPrice;
    public final String rfqId;
    
    public PriceQuote(final String quoterId, final String rfqId, final String itemId, final Double retailPrice, final Double price) {
      this.quoterId = quoterId;
      this.rfqId = rfqId;
      this.itemId = itemId;
      this.retailPrice = retailPrice;
      this.price = price;
    }

    @Override
    public String toString() {
      return "PriceQuote[quoterId=" + quoterId + " rfqId=" + rfqId + " itemId=" + itemId + " retailPrice=" + retailPrice + " price=" + price + "]";
    }
  }
}
