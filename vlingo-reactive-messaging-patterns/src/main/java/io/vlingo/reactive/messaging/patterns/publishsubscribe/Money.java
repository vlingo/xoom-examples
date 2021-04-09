// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.publishsubscribe;

import java.math.BigDecimal;

public class Money {

    private final BigDecimal amount;

    public Money(final String amount) {
        this.amount = new BigDecimal(amount).setScale(4, BigDecimal.ROUND_HALF_UP);
    }

    public String toString() {
        return amount.toPlainString();
    }
}
