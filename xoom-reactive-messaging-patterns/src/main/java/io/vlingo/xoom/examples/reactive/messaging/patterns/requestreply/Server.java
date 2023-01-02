// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.reactive.messaging.patterns.requestreply;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.common.Completes;

public class Server extends Actor implements Service {

  @Override
  public void requestFor(final String what, final Consumer consumer) {
    consumer.replyOf("Service request response for: " + what);
  }

  @Override
  public Completes<String> query(final String what) {
    return completes().with("Service query response for: " + what);
  }
}
