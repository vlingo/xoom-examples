// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.recipientlist;

import io.vlingo.reactive.messaging.patterns.recipientlist.PriceQuoteInterest.PriceQuote;

/**
 * OrderProcessor behavior for registering interest and quoting discounts for baskets of retail items.
 */
public interface OrderProcessor
{
    void register( PriceQuoteInterest interest );
    void requestForQuote( RetailBasket basket );
    void remittedPriceQuote( PriceQuote quote );
}
