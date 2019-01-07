// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.returnaddress;

import io.vlingo.actors.Actor;

public class Server extends Actor implements Service {
  private final ComplexService worker;

  public Server() {
    worker = stage().actorFor(ComplexService.class, Worker.class);
  }

  @Override
  public void simpleRequestFor(final String what, final Consumer consumer) {
    consumer.replyToSimple("SimpleService response for: " + what);
  }

  @Override
  public void complexRequestFor(final String what, final Consumer consumer) {
    worker.complexRequestFor(what, consumer);
  }

  public static class Worker extends Actor implements ComplexService {
    @Override
    public void complexRequestFor(final String what, final Consumer consumer) {
      consumer.replyToComplex("Worker ComplexService response for: " + what);
    }
  }
}
