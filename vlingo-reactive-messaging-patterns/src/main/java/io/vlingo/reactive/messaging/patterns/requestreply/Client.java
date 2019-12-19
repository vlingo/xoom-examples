// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.requestreply;

import io.vlingo.actors.Actor;

public class Client extends Actor implements Consumer {
  private final Service service;
  private final RequestReplyResults results;

  public Client(final Service service, final RequestReplyResults results) {
    this.service = service;
    this.results = results;

    service.requestFor("Request from Client-Consumer!", selfAs(Consumer.class));
  }

  @Override
  public void replyOf(final String what) {
    logger().debug("Consumer received request-reply of: " + what);
    results.access.writeUsing("afterReplyReceivedCount", 1);

    service.query("Query from Client-Consumer!").andFinallyConsume(result -> {
      logger().debug("Consumer received query-reply of: " + what);
      results.access.writeUsing("afterQueryPerformedCount", 1);
    });
  }
}
