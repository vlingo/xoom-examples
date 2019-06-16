// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.returnaddress;

import io.vlingo.actors.Actor;

public class Client extends Actor implements Consumer {
  private final ReturnAddressResults results;

  public Client(final Service service, final ReturnAddressResults results) {
    this.results = results;

    service.simpleRequestFor("Simple Request from Client-Consumer!", selfAs(Consumer.class));
    service.complexRequestFor("Complex Request from Client-Consumer!", selfAs(Consumer.class));
  }

  @Override
  public void replyToSimple(final String what) {
    logger().debug("Consumer received reply-to-simple: " + what);
    results.access.writeUsing("afterSimpleReplyCount", 1);
  }

  @Override
  public void replyToComplex(final String what) {
    logger().debug("Consumer received reply-to-complex: " + what);
    results.access.writeUsing("afterComplexReplyCount", 1);
  }
}
