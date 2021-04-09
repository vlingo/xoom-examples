// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.aggregator;

import io.vlingo.reactive.messaging.patterns.aggregator.PriceQuotes.PriceQuote;

public interface PriceQuoteAggregator {
  void priceQuoteFulfilled(final PriceQuote priceQuote);
  void requiredQuotesFulfillment(final String rfqId, final int totalQuotesRequested);
}
