package io.vlingo.cars.persistence;

import io.vlingo.actors.ActorInstantiator;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Protocols;
import io.vlingo.actors.World;
import io.vlingo.lattice.model.projection.ProjectionDispatcher;
import io.vlingo.lattice.model.projection.TextProjectionDispatcherActor;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.cars.CarConfig;
import io.vlingo.cars.model.CarEntity;
import io.vlingo.cars.model.CarEvents;
import io.vlingo.cars.query.CarProjection;
import io.vlingo.cars.query.CarQueries;
import io.vlingo.cars.query.CarQueriesActor;
import io.vlingo.cars.query.CarsProjection;
import io.vlingo.cars.query.view.CarView;
import io.vlingo.cars.query.view.CarsView;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.DataFormat;
import io.vlingo.symbio.store.common.jdbc.Configuration;
import io.vlingo.symbio.store.common.jdbc.postgres.PostgresConfigurationProvider;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.dispatch.control.DispatcherControlActor;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.jdbc.JDBCDispatcherControlDelegate;
import io.vlingo.symbio.store.journal.jdbc.JDBCJournalActor;
import io.vlingo.symbio.store.journal.jdbc.JDBCJournalInstantWriter;
import io.vlingo.symbio.store.journal.jdbc.JDBCJournalWriter;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.jdbc.JDBCEntriesInstantWriter;
import io.vlingo.symbio.store.state.jdbc.JDBCStateStoreActor;
import io.vlingo.symbio.store.state.jdbc.JDBCStorageDelegate;
import io.vlingo.symbio.store.state.jdbc.postgres.PostgresStorageDelegate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StorageProvider {
    private static final int MAXIMUM_RETRIES = 5;

    private static StorageProvider instance;

    public final Journal<String> journal;
    public final StateStore stateStore;
    public final CarQueries carQueries;

    public static StorageProvider instance() {
        return instance;
    }

    public static StorageProvider using(World world, CarConfig config) throws Exception {
        if (instance != null) {
            return instance;
        }

        // PROJECTIONS
        StateStore stateStore = newStateStore(world, config);
        List<ProjectionDispatcher.ProjectToDescription> descriptions = Arrays.asList(
                ProjectionDispatcher.ProjectToDescription.with(CarProjection.class, Optional.of(stateStore), CarEvents.CarDefined.class),
                ProjectionDispatcher.ProjectToDescription.with(CarProjection.class, Optional.of(stateStore), CarEvents.CarRegistered.class),
                ProjectionDispatcher.ProjectToDescription.with(CarsProjection.class, Optional.of(stateStore), CarEvents.CarDefined.class),
                ProjectionDispatcher.ProjectToDescription.with(CarsProjection.class, Optional.of(stateStore), CarEvents.CarRegistered.class));
        Protocols dispatcherProtocols = world.stage().actorFor(
                new Class<?>[] { Dispatcher.class, ProjectionDispatcher.class },
                Definition.has(TextProjectionDispatcherActor.class, Definition.parameters(descriptions)));
        Protocols.Two<Dispatcher, ProjectionDispatcher> dispatchers = Protocols.two(dispatcherProtocols);


        // COMMANDS
        Journal<String> journal = newJournal(world, config, dispatchers._1);
        SourcedTypeRegistry registry = new SourcedTypeRegistry(world);

        registry.register(new SourcedTypeRegistry.Info(journal, CarEntity.class, CarEntity.class.getSimpleName()));
        registry.info(CarEntity.class)
                .registerEntryAdapter(CarEvents.CarDefined.class, new EventAdapter<>(CarEvents.CarDefined.class))
                .registerEntryAdapter(CarEvents.CarRegistered.class, new EventAdapter<>(CarEvents.CarRegistered.class));

        // QUERIES
        CarQueries carQueries = world.stage().actorFor(CarQueries.class, CarQueriesActor.class, stateStore);

        instance = new StorageProvider(journal, stateStore, carQueries);

        return instance;
    }

    private static Journal<String> newJournal(World world, CarConfig config, Dispatcher dispatcher) throws Exception {
        final Configuration databaseConfiguration = buildDatabaseConfiguration(world, config);

        final JDBCDispatcherControlDelegate dispatcherControlDelegate =
                new JDBCDispatcherControlDelegate(Configuration.cloneOf(databaseConfiguration), world.defaultLogger());

        final List<Dispatcher<Dispatchable<Entry<String>, State.TextState>>> dispatchers = dispatcher == null
                ? Collections.emptyList()
                : Collections.singletonList(typed(dispatcher));

        DispatcherControl dispatcherControl = world.stage().actorFor(DispatcherControl.class,
                Definition.has(DispatcherControlActor.class,
                        new DispatcherControl.DispatcherControlInstantiator(
                                dispatchers,
                                dispatcherControlDelegate,
                                5000,
                                5000)));

        JDBCJournalWriter journalWriter = new JDBCJournalInstantWriter(databaseConfiguration, dispatchers, dispatcherControl);

        Journal<String> journal = world.stage().actorFor(Journal.class, JDBCJournalActor.class, databaseConfiguration, journalWriter);

        return journal;
    }

    private static Configuration buildDatabaseConfiguration(World world, CarConfig config) throws Exception {
        Exception connectionException = null;

        for (int retryCount = 1; retryCount <= MAXIMUM_RETRIES; retryCount++) {
            try {
                world.defaultLogger().info("[Attempt {}] Connecting to database...", retryCount);

                return PostgresConfigurationProvider.configuration(
                        DataFormat.Text,
                        config.databaseUrl,
                        config.databaseName,
                        config.databaseUsername,
                        config.databasePassword,
                        config.databaseOriginator,
                        true);

            } catch (final Exception exception) {
                world.defaultLogger().error(exception.getMessage());
                connectionException = exception;
                Thread.sleep(5000);
            }
        }

        throw connectionException;
    }

    private static StateStore newStateStore(World world, CarConfig config) throws Exception {
        final Configuration databaseConfiguration = buildDatabaseConfiguration(world, config);

        final JDBCStorageDelegate<?> delegate =
                new PostgresStorageDelegate(databaseConfiguration, world.defaultLogger());

        final JDBCEntriesInstantWriter entriesWriter =
                new JDBCEntriesInstantWriter(typed(delegate), null, null);

        final ActorInstantiator instantiator =
                new JDBCStateStoreActor.JDBCStateStoreInstantiator(typed(delegate), entriesWriter, new StateStoreInitializationPrimer(world));

        return world.actorFor(StateStore.class, JDBCStateStoreActor.class, instantiator);
    }

    @SuppressWarnings({"unchecked"})
    private static JDBCStorageDelegate<State.TextState> typed(final JDBCStorageDelegate<?> delegate) {
        return (JDBCStorageDelegate<State.TextState>) delegate;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static Dispatcher<Dispatchable<Entry<String>, State.TextState>> typed(Dispatcher dispatcher) {
        return dispatcher;
    }

    private StorageProvider(Journal<String> journal, StateStore stateStore, CarQueries carQueries) {
        this.journal = journal;
        this.stateStore = stateStore;
        this.carQueries = carQueries;
    }

    private static class StateStoreInitializationPrimer implements StateStore.InitializationPrimer {
        private final World world;

        private StateStoreInitializationPrimer(World world) {
            this.world = world;
        }

        @Override
        public void prime(StateStore stateStore) {
            registerStatefulTypes(stateStore);
        }

        private void registerStatefulTypes(StateStore stateStore) {
            final StatefulTypeRegistry registry = new StatefulTypeRegistry(world);

            registry
                    .register(new StatefulTypeRegistry.Info<>(stateStore, CarView.class, CarView.class.getSimpleName()))
                    .register(new StatefulTypeRegistry.Info<>(stateStore, CarsView.class, CarsView.class.getSimpleName()));
        }
    }
}
