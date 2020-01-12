// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.aggregator;

public interface RequestForQuotationSupplier {
  void supplyPriceQuoteRequestsFor(final PriceQuoteInterest interest);

  public final class PriceQuoteInterest {
    public final String id;
    public final PriceQuotes priceQuotes;
    public final Double lowTotalRetail;
    public final Double highTotalRetail;

    public PriceQuoteInterest(final String interestId, final PriceQuotes priceQuotes, final Double lowTotalRetail, final Double highTotalRetail) {
      this.id = interestId;
      this.priceQuotes = priceQuotes;
      this.lowTotalRetail = lowTotalRetail;
      this.highTotalRetail = highTotalRetail;
    }
  }
}
