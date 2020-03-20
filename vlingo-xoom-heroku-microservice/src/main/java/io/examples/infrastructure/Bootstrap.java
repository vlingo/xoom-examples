// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.


package io.examples.infrastructure;

import io.examples.calculation.domain.Calculation;
import io.examples.calculation.domain.CalculationState;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import io.vlingo.actors.World;
import io.vlingo.http.resource.Server;
import io.vlingo.lattice.model.object.ObjectTypeRegistry;
import io.vlingo.lattice.model.object.ObjectTypeRegistry.Info;
import io.vlingo.symbio.store.MapQueryExpression;
import io.vlingo.symbio.store.object.ObjectStore;
import io.vlingo.symbio.store.object.StateObjectMapper;
import io.vlingo.symbio.store.object.inmemory.InMemoryObjectStoreActor;
import io.vlingo.xoom.VlingoServer;

public class Bootstrap {

    private static Bootstrap instance;
    private Server server;
    private World world;

    public static void main(String[] args) {
        Bootstrap.boot();
    }

    public static void boot() {
        Bootstrap.instance();
    }

    public static Bootstrap instance() {
        if (instance == null) {
            final ApplicationContext applicationContext = run();

            final VlingoServer vlingoServer =
                    applicationContext.getBean(VlingoServer.class);

            instance = new Bootstrap(vlingoServer);
        }
        return instance;
    }

    private static ApplicationContext run() {
        return Micronaut.run(Bootstrap.class);
    }

    private Bootstrap(final VlingoServer vlingoServer) {
        this.server = vlingoServer.getServer();
        this.world = vlingoServer.getVlingoScene().getWorld();

        final MapQueryExpression objectQuery =
                MapQueryExpression.using(Calculation.class, "find", MapQueryExpression.map("id", "id"));

        final ObjectStore objectStore =
                world.stage().actorFor(ObjectStore.class, InMemoryObjectStoreActor.class, new MockDispatcher());

        final StateObjectMapper stateObjectMapper =
                StateObjectMapper.with(Calculation.class, new Object(), new Object());

        final Info<CalculationState> info =
                new Info(objectStore, CalculationState.class,
                        "ObjectStore", objectQuery, stateObjectMapper);

        new ObjectTypeRegistry(world).register(info);

        CalculationQueryProvider.using(world.stage(), objectStore);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stopAndCleanup();
        }));
    }

    public void stopAndCleanup() {
        instance = null;
        world.terminate();
        server.stop();
        CalculationQueryProvider.reset();
        pause();
    }

    public World world() {
        return world;
    }

    private void pause() {
        try {
            Thread.sleep(1000L);
        } catch (Exception e) {
            // ignore
        }
    }
}
