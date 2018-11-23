// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.scattergather;

/**
 * QuoteSubscriptionRequest
 *
 * @author brsg.io
 * @since Nov 21, 2018
 */
public class QuoteSubscriptionRequest
{
    public final String quoterId;
    public final QuoteProcessor quoteProcessor;
    
    public QuoteSubscriptionRequest( String quoterId, QuoteProcessor quoteProcessor )
    {
        this.quoterId = quoterId;
        this.quoteProcessor = quoteProcessor;
    }
}
