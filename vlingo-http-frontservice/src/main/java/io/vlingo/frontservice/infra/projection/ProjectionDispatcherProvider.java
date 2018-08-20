// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.frontservice.infra.projection;

import java.util.Arrays;
import java.util.List;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Protocols;
import io.vlingo.actors.Stage;
import io.vlingo.symbio.projection.ProjectionDispatcher;
import io.vlingo.symbio.projection.ProjectionDispatcher.ProjectToDescription;
import io.vlingo.symbio.projection.state.TextStateProjectionDispatcherActor;
import io.vlingo.symbio.store.state.TextStateStore.TextDispatcher;

public class ProjectionDispatcherProvider {
  private static ProjectionDispatcherProvider instance;

  public final ProjectionDispatcher projectionDispatcher;
  public final TextDispatcher textStateStoreDispatcher;

  public static ProjectionDispatcherProvider instance() {
    return instance;
  }

  public static ProjectionDispatcherProvider using(final Stage stage) {
    if (instance != null) return instance;

    final List<ProjectToDescription> descriptions =
            Arrays.asList(
                    new ProjectToDescription(UserProjectionActor.class, "User:new;User:contact;User:name"),
                    new ProjectToDescription(ProfileProjectionActor.class, "Profile:new;Profile:twitter;Profile:linkedIn;Profile:website"));

    final Protocols dispatcherProtocols =
            stage.actorFor(
                    Definition.has(TextStateProjectionDispatcherActor.class, Definition.parameters(descriptions)),
                    new Class<?>[] { TextDispatcher.class, ProjectionDispatcher.class });

    final Protocols.Two<TextDispatcher, ProjectionDispatcher> dispatchers = Protocols.two(dispatcherProtocols);

    instance = new ProjectionDispatcherProvider(dispatchers._1, dispatchers._2);

    return instance;
  }

  private ProjectionDispatcherProvider(final TextDispatcher textStateStoreDispatcher, final ProjectionDispatcher projectionDispatcher) {
    this.textStateStoreDispatcher = textStateStoreDispatcher;
    this.projectionDispatcher = projectionDispatcher;
  }
}
