// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.aggregator.quotes;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.examples.reactive.messaging.patterns.aggregator.PriceQuoteRequester;
import io.vlingo.xoom.examples.reactive.messaging.patterns.aggregator.PriceQuotes;
import io.vlingo.xoom.examples.reactive.messaging.patterns.aggregator.RequestForQuotationSupplier;
import io.vlingo.xoom.examples.reactive.messaging.patterns.aggregator.RequestForQuotationSupplier.PriceQuoteInterest;

public class MountainAscentPriceQuotesActor extends Actor implements PriceQuotes {
  private final String id;

  public MountainAscentPriceQuotesActor(final RequestForQuotationSupplier supplier) {
    this.id = "MountainAscent(" + address().idString() + ")";
    supplier.supplyPriceQuoteRequestsFor(new PriceQuoteInterest(id, selfAs(PriceQuotes.class), 70.00, 5000.00));
  }

  @Override
  public void requestPriceQuote(final PriceQuoteRequest request, final PriceQuoteRequester requester) {
    final Double discount = discountPercentage(request.orderTotalRetailPrice) * request.retailPrice;
    requester.priceQuoteCompleted(new PriceQuote(id, request.rfqId, request.itemId, request.retailPrice, request.retailPrice - discount));
  }

  private Double discountPercentage(final Double orderTotalRetailPrice) {
    if (orderTotalRetailPrice <= 99.99) return 0.01;
    else if (orderTotalRetailPrice <= 199.99) return 0.02;
    else if (orderTotalRetailPrice <= 499.99) return 0.03;
    else if (orderTotalRetailPrice <= 799.99) return 0.04;
    else if (orderTotalRetailPrice <= 999.99) return 0.045;
    else if (orderTotalRetailPrice <= 2999.99) return 0.0475;
    else return 0.05;
  }
}
