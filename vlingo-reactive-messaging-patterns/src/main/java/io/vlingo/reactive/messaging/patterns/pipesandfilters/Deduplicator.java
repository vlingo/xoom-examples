// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.pipesandfilters;

import io.vlingo.actors.Actor;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class Deduplicator extends Actor implements OrderProcessor {
  private final OrderProcessor nextFilter;
  private final Set<String> processedOrderIds;
  private final PipeAndFilterResults results;

  public Deduplicator(final OrderProcessor nextFilter, final PipeAndFilterResults results) {
    this.nextFilter = nextFilter;
    this.results = results;
    this.processedOrderIds = new HashSet<>();
  }

  @Override
  public void processIncomingOrder(final byte[] orderInfo) {
    final String textOrderInfo = new String(orderInfo, StandardCharsets.UTF_8);
    logger().debug("Deduplicator: processing: " + textOrderInfo);
    final String orderId = orderIdFrom(textOrderInfo);
    if (processedOrderIds.add(orderId)) {
      nextFilter.processIncomingOrder(orderInfo);
    } else {
      logger().debug("Deduplicator: found duplicate order " + orderId);
    }
    results.access.writeUsing("afterOrderDeduplicatedCount", 1);
  }

  private String orderIdFrom(final String textOrderInfo) {
    final int orderIdIndex = textOrderInfo.indexOf("id='") + 4;
    final int orderIdLastIndex = textOrderInfo.indexOf("'", orderIdIndex);
    return textOrderInfo.substring(orderIdIndex, orderIdLastIndex);
  }
}
