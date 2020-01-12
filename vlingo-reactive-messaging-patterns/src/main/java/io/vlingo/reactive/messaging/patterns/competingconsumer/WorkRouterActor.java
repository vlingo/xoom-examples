// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.competingconsumer;

import io.vlingo.actors.Definition;
import io.vlingo.actors.RoundRobinRouter;
import io.vlingo.actors.Router;
import io.vlingo.actors.RouterSpecification;
/**
 * WorkRouterActor is a {@link Router} that routes {@link WorkItem}s
 * to {@link WorkConsumer}s, specifically to {@link WorkConsumerActor}s.
 */
public class WorkRouterActor extends RoundRobinRouter<WorkConsumer> implements WorkConsumer {
  
  public WorkRouterActor(final int poolSize, final CompetingConsumerResults results) {
    super(
            new RouterSpecification<WorkConsumer>(
              poolSize,
              Definition.has(WorkConsumerActor.class, Definition.parameters(results)),
              WorkConsumer.class
            )
          );
  }

  /* @see io.vlingo.reactive.messaging.patterns.competingconsumer.WorkConsumer#consume(io.vlingo.reactive.messaging.patterns.competingconsumer.WorkItem) */
  @Override
  public void consumeWork(final WorkItem item) {
    dispatchCommand(WorkConsumer::consumeWork, item);
  }
}
