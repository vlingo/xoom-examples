// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.publishsubscribe;

public class PriceQuoted {

    private final Market market;
    private final String ticker;
    private final Money money;

    public PriceQuoted(final Market market, final String ticker, final Money money) {
        this.market = market;
        this.ticker = ticker;
        this.money = money;
    }

    public Market market() {
        return market;
    }

    public String ticker() {
        return ticker;
    }

    public Money money() {
        return money;
    }
}
