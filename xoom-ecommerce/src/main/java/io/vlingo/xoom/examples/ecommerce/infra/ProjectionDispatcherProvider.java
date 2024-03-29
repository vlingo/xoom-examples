// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.ecommerce.infra;

import java.util.Arrays;
import java.util.List;

import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Protocols;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.examples.ecommerce.model.CartEvents.AllItemsRemovedEvent;
import io.vlingo.xoom.examples.ecommerce.model.CartEvents.CreatedForUser;
import io.vlingo.xoom.examples.ecommerce.model.CartEvents.ProductQuantityChangeEvent;
import io.vlingo.xoom.lattice.model.projection.ProjectionDispatcher;
import io.vlingo.xoom.lattice.model.projection.ProjectionDispatcher.ProjectToDescription;
import io.vlingo.xoom.lattice.model.projection.TextProjectionDispatcherActor;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;

@SuppressWarnings("rawtypes")
public class ProjectionDispatcherProvider {
  private static ProjectionDispatcherProvider instance;

  public final ProjectionDispatcher projectionDispatcher;
  public final Dispatcher storeDispatcher;

  public static ProjectionDispatcherProvider instance() {
    return instance;
  }

  public static ProjectionDispatcherProvider using(final Stage stage) {

    if (instance == null) {
      final List<ProjectToDescription> descriptions =
              Arrays.asList(
            		  new ProjectToDescription(
            				  CartSummaryProjectionActor.class,
            				  CreatedForUser.class.getName(),
            				  ProductQuantityChangeEvent.class.getName(),
            				  AllItemsRemovedEvent.class.getName()));

      final Protocols dispatcherProtocols =
              stage.actorFor(
                      new Class<?>[]{Dispatcher.class, ProjectionDispatcher.class},
                      Definition.has(TextProjectionDispatcherActor.class, Definition.parameters(descriptions)));

      final Protocols.Two<Dispatcher, ProjectionDispatcher> dispatchers = Protocols.two(dispatcherProtocols);
      final Dispatcher storeDispatcher = dispatchers._1;
      final ProjectionDispatcher projectionDispatcher = dispatchers._2;

      instance = new ProjectionDispatcherProvider(storeDispatcher, projectionDispatcher);
    }
    return instance;
  }

  public static void deleteInstance() {
    instance = null;
  }

  private ProjectionDispatcherProvider(final Dispatcher storeDispatcher, final ProjectionDispatcher projectionDispatcher) {
    this.storeDispatcher = storeDispatcher;
    this.projectionDispatcher = projectionDispatcher;
  }

}
