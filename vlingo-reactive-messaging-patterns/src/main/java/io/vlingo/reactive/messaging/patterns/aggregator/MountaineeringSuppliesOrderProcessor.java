// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.aggregator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Definition;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.reactive.messaging.patterns.aggregator.PriceQuotes.PriceQuote;
import io.vlingo.reactive.messaging.patterns.aggregator.PriceQuotes.PriceQuoteRequest;

public class MountaineeringSuppliesOrderProcessor extends Actor
    implements RequestForQuotationProcessor, RequestForQuotationSupplier,
               PriceQuoteRequester, PriceQuotesFulfillmentWatcher {

  private final PriceQuoteAggregator aggregator;
  private final Map<String,PriceQuoteInterest> interests;
  private final PriceQuoteRequester requester;
  private final TestUntil until;

  public MountaineeringSuppliesOrderProcessor(final TestUntil until) {
    this.until = until;
    this.interests = new HashMap<>();
    this.requester = selfAs(PriceQuoteRequester.class);
    this.aggregator =
            stage().actorFor(
                    Definition.has(
                            PriceQuoteAggregatorActor.class,
                            Definition.parameters(selfAs(PriceQuotesFulfillmentWatcher.class))),
                    PriceQuoteAggregator.class);
  }

  //========================================
  // PriceQuoteRequester
  //========================================

  @Override
  public void priceQuoteCompleted(final PriceQuote priceQuote) {
    aggregator.priceQuoteFulfilled(priceQuote);
  }

  //========================================
  // RequestForQuotationProcessor
  //========================================

  @Override
  public void requestPriceQuotationFor(final RequestForQuotation rfq) {
    final List<PriceQuoteInterest> recipientList = calculateRecipientList(rfq);
    aggregator.requiredQuotesFulfillment(rfq.rfqId, recipientList.size() * rfq.retailItems.size());
    dispatchTo(rfq, recipientList);
  }

  //========================================
  // RequestForQuotationSupplier
  //========================================

  @Override
  public void supplyPriceQuoteRequestsFor(final PriceQuoteInterest interest) {
    interests.put(interest.id, interest);
  }

  //========================================
  // PriceQuotesFulfillmentWatcher
  //========================================

  @Override
  public void quotationFulfillmentCompleted(final QuotationFulfillment fulfillment) {
    until.happened();
  }

  //========================================
  // internal implementation
  //========================================

  private List<PriceQuoteInterest> calculateRecipientList(final RequestForQuotation rfq) {
    final List<PriceQuoteInterest> priceQuotesInterests = new ArrayList<>();
    for (final PriceQuoteInterest interest : interests.values()) {
      if (rfq.totalRetailPrice >= interest.lowTotalRetail &&
          rfq.totalRetailPrice <= interest.highTotalRetail) {
        priceQuotesInterests.add(interest);
      }
    }
    return priceQuotesInterests;
  }

  private void dispatchTo(final RequestForQuotation rfq, Iterable<PriceQuoteInterest> recipientList) {
    recipientList.forEach(recipient -> {
      rfq.retailItems.forEach(retailItem -> {
        System.out.println("OrderProcessor: " + rfq.rfqId + " item: " + retailItem.itemId + " to: " + recipient.id);
        recipient.priceQuotes.requestPriceQuote(new PriceQuoteRequest(rfq.rfqId, retailItem.itemId, retailItem.retailPrice, rfq.totalRetailPrice), requester);
      });
    });
  }
}
