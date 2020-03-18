// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.


package io.examples.infrastructure;

import io.examples.calculation.domain.CalculationState;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import io.vlingo.actors.World;
import io.vlingo.http.resource.Server;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.symbio.store.state.inmemory.InMemoryStateStoreActor;
import io.vlingo.xoom.VlingoServer;

import static java.util.Collections.emptyList;

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

        final StateStore stateStore =
                world.stage().actorFor(StateStore.class, InMemoryStateStoreActor.class, emptyList());

        new StatefulTypeRegistry(world)
                .register(new Info(stateStore, CalculationState.class, "StateStore"));

        CalculationQueryProvider.using(world.stage(), stateStore);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stopAndCleanup();
        }));
    }

    public void stopAndCleanup() {
        instance = null;
        world.terminate();
        server.stop();
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
