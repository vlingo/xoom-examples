package io.vlingo.perf.vlingo.infrastructure.persistence;

import io.vlingo.actors.Actor;
import io.vlingo.actors.ActorInstantiator;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.common.Tuple2;
import io.vlingo.symbio.store.common.jdbc.Configuration;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.symbio.store.dispatch.control.DispatcherControlActor;
import io.vlingo.symbio.store.state.PartitioningStateStore;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.jdbc.JDBCEntriesBatchWriter;
import io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor;
import io.vlingo.symbio.store.state.jdbc.JDBCStorageDelegate;
import io.vlingo.symbio.store.state.jdbc.postgres.PostgresStorageDelegate;

import java.util.Collections;
import java.util.Optional;

public class StorageProvider {
    private static final long DefaultCheckConfirmationExpirationInterval = 5000;
    private static final long DefaultConfirmationExpiration = 5000;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Tuple2<StateStore, DispatcherControl> storeWithControl(final Stage stage, final Configuration configuration, Dispatcher dispatcher) {
        final StateStore.StorageDelegate delegate = new PostgresStorageDelegate(configuration, stage.world().defaultLogger());

        final DispatcherControl dispatcherControl = stage.actorFor(DispatcherControl.class,
                Definition.has(DispatcherControlActor.class,
                        new DispatcherControl.DispatcherControlInstantiator(dispatcher,
                                (DispatcherControl.DispatcherControlDelegate) delegate,
                                DefaultCheckConfirmationExpirationInterval,
                                DefaultConfirmationExpiration)));

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
                PartitioningStateStore.using(stage, JDBCStateStoreActor.class, instantiatorProvider, 10, 10),
                dispatcherControl);
    }

    public static Tuple2<StateStore, DispatcherControl> storeWithControl(final Stage stage, final Configuration config) {
        return storeWithControl(stage, config, new NoOpDispatcher());
    }
}
