// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.pipesandfilters;

import io.vlingo.xoom.actors.Actor;

import java.nio.charset.StandardCharsets;

public class OrderManagementSystem extends Actor implements OrderProcessor {
  private final PipeAndFilterResults results;

  public OrderManagementSystem(final PipeAndFilterResults results) {
    this.results = results;
  }

  @Override
  public void processIncomingOrder(byte[] orderInfo) {
    final String textOrderInfo = new String(orderInfo, StandardCharsets.UTF_8);
    logger().debug("OrderManagementSystem: processing unique order:" + textOrderInfo);
    results.access.writeUsing("afterOrderManagedCount", 1);
  }
}
