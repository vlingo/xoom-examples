// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.pipesandfilters;

import io.vlingo.actors.Actor;

import java.nio.charset.StandardCharsets;

public class Authenticator extends Actor implements OrderProcessor {
  private final OrderProcessor nextFilter;
  private final PipeAndFilterResults results;

  public Authenticator(final OrderProcessor nextFilter, final PipeAndFilterResults results) {
    this.nextFilter = nextFilter;
    this.results = results;
  }

  @Override
  public void processIncomingOrder(final byte[] orderInfo) {
    final String textOrderInfo = new String(orderInfo, StandardCharsets.UTF_8);
    logger().debug("Authenticator: processing: " + textOrderInfo);
    final String orderText = textOrderInfo.replace("(certificate)", "");
    nextFilter.processIncomingOrder(orderText.getBytes());
    results.access.writeUsing("afterOrderAuthenticatedCount", 1);
  }
}
