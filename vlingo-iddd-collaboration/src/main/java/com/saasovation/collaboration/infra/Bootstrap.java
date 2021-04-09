// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.collaboration.infra;

import com.saasovation.collaboration.infra.dispatch.ExchangeDispatcher;
import com.saasovation.collaboration.infra.exchange.ExchangeBootstrap;
import com.saasovation.collaboration.infra.persistence.SourcedRegistration;
import io.vlingo.actors.World;
import io.vlingo.lattice.exchange.Exchange;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;

import java.util.Arrays;

public class Bootstrap {

  @SuppressWarnings("rawtypes")
  private Bootstrap() throws Exception {
    final World world = World.startWithDefaults("agile-collaboration");

    final Exchange camelExchange = new ExchangeBootstrap(world).initExchange();
    final ExchangeDispatcher dispatcher = new ExchangeDispatcher(camelExchange);
    final Journal journal = world.actorFor(Journal.class, InMemoryJournalActor.class, Arrays.asList(dispatcher));
    final SourcedTypeRegistry registry = new SourcedTypeRegistry(world);

    SourcedRegistration.registerAllWith(registry, journal);
  }

  public static void main(String[] args) throws Exception {
    new Bootstrap();
  }
}
