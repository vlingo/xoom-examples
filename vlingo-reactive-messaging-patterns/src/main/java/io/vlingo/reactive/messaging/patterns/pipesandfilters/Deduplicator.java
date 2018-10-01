// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.pipesandfilters;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;

public class Deduplicator extends Actor implements OrderProcessor {
  private final OrderProcessor nextFilter;
  private final Set<String> processedOrderIds;
  private final TestUntil until;

  public Deduplicator(final OrderProcessor nextFilter, final TestUntil until) {
    this.nextFilter = nextFilter;
    this.until = until;
    this.processedOrderIds = new HashSet<>();
  }

  @Override
  public void processIncomingOrder(final byte[] orderInfo) {
    final String textOrderInfo = new String(orderInfo, StandardCharsets.UTF_8);
    logger().log("Deduplicator: processing: " + textOrderInfo);
    final String orderId = orderIdFrom(textOrderInfo);
    if (processedOrderIds.add(orderId)) {
      nextFilter.processIncomingOrder(orderInfo);
    } else {
      logger().log("Deduplicator: found duplicate order " + orderId);
    }
    until.happened();
  }

  private String orderIdFrom(final String textOrderInfo) {
    final int orderIdIndex = textOrderInfo.indexOf("id='") + 4;
    final int orderIdLastIndex = textOrderInfo.indexOf("'", orderIdIndex);
    return textOrderInfo.substring(orderIdIndex, orderIdLastIndex);
  }
}
