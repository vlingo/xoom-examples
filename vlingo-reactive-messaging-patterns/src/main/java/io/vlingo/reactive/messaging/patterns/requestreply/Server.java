// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.reactive.messaging.patterns.requestreply;

import io.vlingo.actors.Actor;
import io.vlingo.common.Completes;

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
