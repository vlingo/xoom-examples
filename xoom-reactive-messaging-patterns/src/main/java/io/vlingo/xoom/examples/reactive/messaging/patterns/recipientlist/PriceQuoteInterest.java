// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.recipientlist;

/**
 * PriceQuoteInterest used to register interest in providing {@link PriceQuote} for each retail item where appropriate.
 */
public class PriceQuoteInterest
{
    public final String type;
    public final QuoteProcessor quoteProcessor;
    public final Double lowTotalRetail;
    public final Double highTotalRetail;
    
    public PriceQuoteInterest( final String type, final QuoteProcessor quoteProcessor, final Double lowTotal, final Double highTotal )
    {
        this.type = type;
        this.quoteProcessor = quoteProcessor;
        this.lowTotalRetail = lowTotal;
        this.highTotalRetail = highTotal;
    }
}
