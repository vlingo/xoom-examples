// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.agilepm.infra;

import com.saasovation.agilepm.infra.dispatch.ExchangeDispatcher;
import com.saasovation.agilepm.infra.exchange.ExchangeBootstrap;
import com.saasovation.agilepm.model.Tenant;
import com.saasovation.agilepm.model.product.Product;
import com.saasovation.agilepm.model.product.ProductEntity;
import com.saasovation.agilepm.model.product.ProductId;
import com.saasovation.agilepm.model.product.ProductOwner;
import io.vlingo.actors.*;
import io.vlingo.common.identity.IdentityGeneratorType;
import io.vlingo.lattice.exchange.Exchange;
import io.vlingo.lattice.grid.GridAddressFactory;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;

public class Bootstrap {
    private final Journal<String> journal;
    private final SourcedTypeRegistry registry;
    private final World world;

    @SuppressWarnings("unchecked")
    private Bootstrap() throws Exception {
        world = World.startWithDefaults("agilepm");

        final Exchange exchange = new ExchangeBootstrap(world).initExchange();

        final ExchangeDispatcher exchangeDispatcher = new ExchangeDispatcher(exchange);

        this.journal = world.actorFor(Journal.class, InMemoryJournalActor.class, exchangeDispatcher);

        registry = new SourcedTypeRegistry(world);

        final SourcedTypeRegistry.Info info = new SourcedTypeRegistry.Info(this.journal, ProductEntity.class, ProductEntity.class.getSimpleName());
        registry.register(info);

        //Do simple operation
        final Tenant tenant = Tenant.with("testTenant");

        final Stage stage = world.stage();
        final AddressFactory addressFactory = new GridAddressFactory(IdentityGeneratorType.RANDOM);

        final ProductId productId = ProductId.unique();
        final Address productAddress = addressFactory.from(productId.id);

        final Product product = stage.actorFor(Product.class, Definition.has(ProductEntity.class, Definition.parameters(tenant, productId)), productAddress);
        product.define(ProductOwner.with(tenant, "ProductOwner"), "test product", "test description", true);

        Thread.sleep(1000);
    }


    public static void main(String[] args) throws Exception {
        new Bootstrap();
    }
}
