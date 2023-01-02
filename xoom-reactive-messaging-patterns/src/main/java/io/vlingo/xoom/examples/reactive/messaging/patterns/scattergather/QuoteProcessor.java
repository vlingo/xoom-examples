// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.scattergather;

/**
 * QuoteProcessor data needed to quote a discount price for an item according to a discount schedule
 * based on total price of a basket of items. 
 */
public interface QuoteProcessor
{
    void requestPriceQuote( String rfqId, String itemId, Double retailPrice, Double totalRetailPrice );
}
