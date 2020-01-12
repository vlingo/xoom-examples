// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.pipesandfilters;

import io.vlingo.actors.Actor;

import java.nio.charset.StandardCharsets;

public class OrderAcceptanceEndpoint extends Actor implements OrderProcessor {
  private final OrderProcessor nextFilter;
  private final PipeAndFilterResults results;

  public OrderAcceptanceEndpoint(final OrderProcessor nextFilter, final PipeAndFilterResults results) {
    this.nextFilter = nextFilter;
    this.results = results;
  }

  @Override
  public void processIncomingOrder(byte[] orderInfo) {
    final String textOrderInfo = new String(orderInfo, StandardCharsets.UTF_8);
    logger().debug("OrderAcceptanceEndpoint: processing " + textOrderInfo);
    nextFilter.processIncomingOrder(orderInfo);
    results.access.writeUsing("afterOrderAcceptedCount", 1);
  }
}
