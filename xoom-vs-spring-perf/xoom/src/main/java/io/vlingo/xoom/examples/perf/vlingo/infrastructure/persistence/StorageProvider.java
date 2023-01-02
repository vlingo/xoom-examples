// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.perf.vlingo.infrastructure.persistence;

import io.vlingo.xoom.actors.Actor;
import io.vlingo.xoom.actors.ActorInstantiator;
import io.vlingo.xoom.actors.Definition;
import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.common.Tuple2;
import io.vlingo.xoom.symbio.store.common.jdbc.Configuration;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;
import io.vlingo.xoom.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.xoom.symbio.store.dispatch.control.DispatcherControlActor;
import io.vlingo.xoom.symbio.store.state.PartitioningStateStore;
import io.vlingo.xoom.symbio.store.state.StateStore;
import io.vlingo.xoom.symbio.store.state.jdbc.JDBCEntriesBatchWriter;
import io.vlingo.xoom.symbio.store.state.jdbc.JDBCStateStoreActor;
import io.vlingo.xoom.symbio.store.state.jdbc.JDBCStorageDelegate;
import io.vlingo.xoom.symbio.store.state.jdbc.postgres.PostgresStorageDelegate;

import java.util.Collections;
import java.util.Optional;

public class StorageProvider {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Tuple2<StateStore, DispatcherControl> storeWithControl(final Stage stage, final Configuration configuration, final Dispatcher dispatcher) {
        final DispatcherControl dispatcherControl = DispatcherControlProvider.using(stage, configuration, dispatcher).dispatcherControl;

        PartitioningStateStore.InstantiatorProvider instantiatorProvider = new PartitioningStateStore.InstantiatorProvider() {
            @Override
            public <A extends Actor> Optional<Definition> definitionFor(Class<A> stateStoreActorType, ActorInstantiator<A> instantiator, PartitioningStateStore.StateStoreRole role, int currentPartition, int totalPartitions) {
                return Optional.of(Definition.has(stateStoreActorType, instantiator, "jdbcQueueMailbox", "StateStore-" + role.name() + "-" + currentPartition));
            }

            @Override
            public <A extends Actor> ActorInstantiator<A> instantiatorFor(Class<A> stateStoreActorType, PartitioningStateStore.StateStoreRole role, int currentPartition, int totalPartitions) {
                final StateStore.StorageDelegate localDelegate = new PostgresStorageDelegate(Configuration.cloneOf(configuration), stage.world().defaultLogger());
                final JDBCEntriesBatchWriter entriesWriter = new JDBCEntriesBatchWriter((JDBCStorageDelegate) localDelegate,
                        Collections.singletonList(dispatcher), dispatcherControl, 1000);
                final JDBCStateStoreActor.JDBCStateStoreInstantiator instantiator = new JDBCStateStoreActor.JDBCStateStoreInstantiator(
                        (JDBCStorageDelegate) localDelegate, entriesWriter, 250, null);

                return (ActorInstantiator<A>) instantiator;
            }
        };

        return Tuple2.from(
                PartitioningStateStore.using(stage, JDBCStateStoreActor.class, instantiatorProvider, 10, 30),
                dispatcherControl);
    }

    public static Tuple2<StateStore, DispatcherControl> storeWithControl(final Stage stage, final Configuration config) {
        return storeWithControl(stage, config, new NoOpDispatcher());
    }

    private static class DispatcherControlProvider {
        private static final long DefaultCheckConfirmationExpirationInterval = 5000;
        private static final long DefaultConfirmationExpiration = 5000;

        private static DispatcherControlProvider instance = null;

        private final DispatcherControl dispatcherControl;

        private DispatcherControlProvider(DispatcherControl dispatcherControl) {
            this.dispatcherControl = dispatcherControl;
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private static DispatcherControlProvider using(final Stage stage, final Configuration configuration, final Dispatcher dispatcher) {
            if (instance != null) return instance;

            final StateStore.StorageDelegate delegate = new PostgresStorageDelegate(configuration, stage.world().defaultLogger());
            final DispatcherControl dispatcherControl = stage.actorFor(DispatcherControl.class,
                    Definition.has(DispatcherControlActor.class,
                            new DispatcherControl.DispatcherControlInstantiator(dispatcher,
                                    (DispatcherControl.DispatcherControlDelegate) delegate,
                                    DefaultCheckConfirmationExpirationInterval,
                                    DefaultConfirmationExpiration)));

            instance = new DispatcherControlProvider(dispatcherControl);

            return instance;
        }
    }
}
