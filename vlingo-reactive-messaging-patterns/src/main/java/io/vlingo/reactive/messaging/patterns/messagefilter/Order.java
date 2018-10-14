// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.messagefilter;

import java.util.Map;

public class Order {

    private final String orderId;
    private final String orderType;
    private final Double grandTotal;
    private final Map<String, OrderItem> orderItems;

    public Order(final String orderId, final String orderType, final Map<String, OrderItem> orderItems)  {
        this.orderId = orderId;
        this.orderType = orderType;
        this.orderItems = orderItems;
        this.grandTotal = orderItems.values().stream().mapToDouble(orderItem -> orderItem.price()).sum();
    }

    public boolean isType(final String orderType) {
        return this.orderType.equals(orderType);
    }

    @Override
    public String toString() {
        return "OrderItem[orderId=" + orderId + " orderType=" + orderType + " totaling=" + grandTotal +"]";
    }

}