// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package com.saasovation.agilepm.infra;

import io.vlingo.actors.World;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.NoOpDispatcher;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.symbio.store.journal.inmemory.InMemoryJournalActor;

public class Bootstrap {
  private final Journal<String> journal;
  private final SourcedTypeRegistry registry;
  private final World world;
  
  @SuppressWarnings("unchecked")
  private Bootstrap() {
    world = World.startWithDefaults("agilepm");
    
    final Dispatcher<Dispatchable<Entry<String>, State<String>>> journalDispatcher = new NoOpDispatcher();
    
    this.journal = world.actorFor(Journal.class, InMemoryJournalActor.class, journalDispatcher);
    
    registry = new SourcedTypeRegistry(world);
  }
}
