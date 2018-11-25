// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.competingconsumer;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;
/**
 * WorkConsumerActor
 */
public class WorkConsumerActor extends Actor implements WorkConsumer {
  
  private final TestUntil until;

  public WorkConsumerActor(final TestUntil until) {
    super();
    this.until = until;
  }

  /* @see io.vlingo.reactive.messaging.patterns.competingconsumer.WorkConsumer#consumeWork(io.vlingo.reactive.messaging.patterns.competingconsumer.WorkItem) */
  @Override
  public void consumeWork(final WorkItem item) {
    logger().log(this + " consumed: " + item);
    until.happened();
  }
}
