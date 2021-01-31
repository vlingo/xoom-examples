/**
 * 
 */
package com.thesis2020.hh.infrastructure.persistence;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.thesis2020.hh.model.greeting.GreetingEvents;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Protocols;
import io.vlingo.actors.Stage;
import io.vlingo.lattice.model.projection.ProjectionDispatcher;
import io.vlingo.lattice.model.projection.ProjectionDispatcher.ProjectToDescription;
import io.vlingo.lattice.model.projection.TextProjectionDispatcherActor;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.state.StateStore;

/**
 * @author hadydab
 *
 */
@SuppressWarnings("rawtypes")
public enum ProjectionDispatcherProvider {

	INSTANCE;

	public ProjectionDispatcher projectionDispatcher;

	public Dispatcher storeDispatcher;

	public void using(final Stage stage,final StateStore stateStore) {
		
		
		final List<ProjectToDescription> descriptions = Arrays
				.asList(new ProjectToDescription(GreetingProjectionActor.class,Optional.of(stateStore), GreetingEvents.GreetingDefined.name(),
						GreetingEvents.GreetingMessageChange.name(), GreetingEvents.GreetingDescriptionChanged.name()));

		final Protocols dispatcherProtocols = stage.actorFor(
				new Class<?>[] { Dispatcher.class, ProjectionDispatcher.class },
				Definition.has(TextProjectionDispatcherActor.class, Arrays.asList(descriptions)));

		final Protocols.Two<Dispatcher, ProjectionDispatcher> dispatchers = Protocols.two(dispatcherProtocols);

		this.storeDispatcher = dispatchers._1;
		this.projectionDispatcher = dispatchers._2;

	}

}
