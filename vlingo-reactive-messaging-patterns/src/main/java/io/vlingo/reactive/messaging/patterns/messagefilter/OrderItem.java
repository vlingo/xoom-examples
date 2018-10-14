// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.messagefilter;

public class OrderItem {

    private final String orderItemId;
    private final String itemType;
    private final String description;
    private final Double price;

    public OrderItem(final String orderItemId, final String itemType, final String description, final Double price) {
        this.orderItemId = orderItemId;
        this.itemType = itemType;
        this.description = description;
        this.price = price;
    }

    public Double price() {
        return price;
    }

    @Override
    public String toString() {
        return "OrderItem[orderItemId=" + orderItemId + " itemType=" + itemType + " description=" + description + " price " + price + "]";
    }
}
