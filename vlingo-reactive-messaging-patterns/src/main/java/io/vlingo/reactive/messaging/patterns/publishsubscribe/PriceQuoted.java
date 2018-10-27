// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.publishsubscribe;

import io.vlingo.actors.pubsub.Publication;
import io.vlingo.actors.pubsub.Topic;

public class PriceQuoted implements Publication {

    private final Market market;
    private final String ticker;
    private final Money money;

    public PriceQuoted(final Market market, final String ticker, final Money money) {
        this.market = market;
        this.ticker = ticker;
        this.money = money;
    }

    @Override
    public Topic topic() {
        return market;
    }

    public String toString() {
        return "PriceQuoted[market="+ market +" ticker= " + ticker + " money= " + money +"]";
    }
}
