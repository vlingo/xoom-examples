// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.messagefilter;

public class OrderPlaced {

    private final Order order;

    public OrderPlaced(final Order order){
        this.order = order;
    }

    public Order order() {
        return order;
    }

    public boolean isType(final String orderType) {
        return this.order.isType(orderType);
    }
}
