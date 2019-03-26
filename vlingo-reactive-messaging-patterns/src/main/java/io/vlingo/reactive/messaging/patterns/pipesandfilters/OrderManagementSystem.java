// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.pipesandfilters;

import java.nio.charset.StandardCharsets;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;

public class OrderManagementSystem extends Actor implements OrderProcessor {
  private final PipeAndFilterResults results;

  public OrderManagementSystem(final PipeAndFilterResults results) {
    this.results = results;
  }

  @Override
  public void processIncomingOrder(byte[] orderInfo) {
    final String textOrderInfo = new String(orderInfo, StandardCharsets.UTF_8);
    logger().log("OrderManagementSystem: processing unique order:" + textOrderInfo);
    results.access.writeUsing("afterOrderManagedCount", 1);
  }
}
