// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.agilepm.infra;

import com.saasovation.agilepm.infra.dispatch.ExchangeDispatcher;
import com.saasovation.agilepm.infra.exchange.ExchangeBootstrap;
import com.saasovation.agilepm.infra.resource.ProductResource;
import com.saasovation.agilepm.model.product.ProductEntity;
import io.vlingo.actors.World;
import io.vlingo.http.resource.Configuration;
import io.vlingo.http.resource.Resources;
import io.vlingo.http.resource.Server;
import io.vlingo.lattice.exchange.Exchange;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;
import java.util.Arrays;

public class Bootstrap {
    private static Bootstrap instance;
    private final World world;
    private final Server server;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Bootstrap() throws Exception {
        world = World.startWithDefaults("agilepm");

        final Exchange exchange = new ExchangeBootstrap(world).initExchange();

        final ExchangeDispatcher exchangeDispatcher = new ExchangeDispatcher(exchange);

        Journal<String> journal = world.actorFor(Journal.class, InMemoryJournalActor.class, Arrays.asList(exchangeDispatcher));

        SourcedTypeRegistry registry = new SourcedTypeRegistry(world);

        final SourcedTypeRegistry.Info info = new SourcedTypeRegistry.Info(journal, ProductEntity.class, ProductEntity.class.getSimpleName());
        registry.register(info);

        final ProductResource productResource = new ProductResource(world);
        final Resources resources = Resources.are(productResource.routes());

        this.server = Server.startWith(world.stage(),
                resources,
                8080,
                Configuration.Sizing.define(),
                Configuration.Timing.define());

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (instance != null) {
                instance.server.stop();

                System.out.println("\n");
                System.out.println("=======================");
                System.out.println("Stopping agilepm-service.");
                System.out.println("=======================");
                pause();
            }
        }));
    }

    public static void main(String[] args) throws Exception {
        System.out.println("=======================");
        System.out.println("service: agilepm-service.");
        System.out.println("=======================");
        Bootstrap.instance();
    }

    static Bootstrap instance() throws Exception {
        if (instance == null) {
            instance = new Bootstrap();
        }
        return instance;
    }

    private void pause() {
        try {
            Thread.sleep(1000L);
        } catch (Exception e) {
            // ignore
        }
    }
}
