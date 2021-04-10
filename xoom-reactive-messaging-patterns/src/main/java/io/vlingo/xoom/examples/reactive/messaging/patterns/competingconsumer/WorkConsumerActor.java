// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.examples.reactive.messaging.patterns.competingconsumer;

import io.vlingo.xoom.actors.Actor;
/**
 * WorkConsumerActor is an actor to which a WorkItem may be routed.
 */
public class WorkConsumerActor extends Actor implements WorkConsumer {
  
  private final CompetingConsumerResults results;

  public WorkConsumerActor(final CompetingConsumerResults results) {
    super();
    this.results = results;
  }

  /* @see io.vlingo.xoom.examples.reactive.messaging.patterns.competingconsumer.WorkConsumer#consumeWork(io.vlingo.xoom.examples.reactive.messaging.patterns.competingconsumer.WorkItem) */
  @Override
  public void consumeWork(final WorkItem item) {
    logger().debug(this + " consumed: " + item);
    results.access.writeUsing("afterItemConsumedCount", 1);
  }
}
