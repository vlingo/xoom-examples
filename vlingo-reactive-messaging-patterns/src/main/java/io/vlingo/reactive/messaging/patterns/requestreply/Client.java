// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.requestreply;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;

public class Client extends Actor implements Consumer {
  private final Service service;
  private final TestUntil until;

  public Client(final Service service, final TestUntil until) {
    this.service = service;
    this.until = until;

    service.requestFor("Request from Client-Consumer!", selfAs(Consumer.class));
  }

  @Override
  public void replyOf(final String what) {
    System.out.println("Consumer received request-reply of: " + what);
    until.happened();

    service.query("Query from Client-Consumer!").after(result -> {
      System.out.println("Consumer received query-reply of: " + what);
      until.happened();
    });
  }
}
