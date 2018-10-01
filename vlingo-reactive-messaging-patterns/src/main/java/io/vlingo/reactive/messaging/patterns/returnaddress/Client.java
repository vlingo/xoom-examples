// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.returnaddress;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;

public class Client extends Actor implements Consumer {
  private final TestUntil until;

  public Client(final Service service, final TestUntil until) {
    this.until = until;

    service.simpleRequestFor("Simple Request from Client-Consumer!", selfAs(Consumer.class));
    service.complexRequestFor("Complex Request from Client-Consumer!", selfAs(Consumer.class));
  }

  @Override
  public void replyToSimple(final String what) {
    System.out.println("Consumer received reply-to-simple: " + what);
    until.happened();
  }

  @Override
  public void replyToComplex(final String what) {
    System.out.println("Consumer received reply-to-complex: " + what);
    until.happened();
  }
}
