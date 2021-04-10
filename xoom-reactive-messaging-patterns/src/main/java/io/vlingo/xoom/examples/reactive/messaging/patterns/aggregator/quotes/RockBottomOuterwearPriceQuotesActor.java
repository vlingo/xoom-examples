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

public class RockBottomOuterwearPriceQuotesActor extends Actor implements PriceQuotes {
  private final String id;

  public RockBottomOuterwearPriceQuotesActor(final RequestForQuotationSupplier supplier) {
    this.id = "RockBottomOuterwear(" + address().idString() + ")";
    supplier.supplyPriceQuoteRequestsFor(new PriceQuoteInterest(id, selfAs(PriceQuotes.class), 0.50, 7500.00));
  }

  @Override
  public void requestPriceQuote(final PriceQuoteRequest request, final PriceQuoteRequester requester) {
    final Double discount = discountPercentage(request.orderTotalRetailPrice) * request.retailPrice;
    requester.priceQuoteCompleted(new PriceQuote(id, request.rfqId, request.itemId, request.retailPrice, request.retailPrice - discount));
  }

  private Double discountPercentage(final Double orderTotalRetailPrice) {
    if (orderTotalRetailPrice <= 100.00) return 0.015;
    else if (orderTotalRetailPrice <= 399.99) return 0.02;
    else if (orderTotalRetailPrice <= 499.99) return 0.03;
    else if (orderTotalRetailPrice <= 799.99) return 0.04;
    else if (orderTotalRetailPrice <= 999.99) return 0.05;
    else if (orderTotalRetailPrice <= 2999.99) return 0.06;
    else if (orderTotalRetailPrice <= 4999.99) return 0.07;
    else if (orderTotalRetailPrice <= 5999.99) return 0.075;
    else return 0.08;
  }
}
