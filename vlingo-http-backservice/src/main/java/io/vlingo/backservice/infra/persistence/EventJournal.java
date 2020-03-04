// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.backservice.infra.persistence;

import java.util.List;

import io.vlingo.actors.Stage;
import io.vlingo.backservice.resource.model.PrivateTokenGenerated;

/**
 * The protocol for event journal has the responsible storing an event via {@link #append(Object)}
 * and then the feed mechanism can pick uo the event and feed it via {@link #streamTo(Sink, Object, int, int)}
 * <p>
 *     The {@link io.vlingo.actors.Actor} instance can be looked up by client via {@link #provider()}
 * </p>
 */
public interface EventJournal {
  Provider provider = new Provider();
  static Provider provider() { return provider; }

  static EventJournal startWith(final Stage stage) {
    provider.set(stage.actorFor(EventJournal.class, EventJournalActor.class));
    return provider.instance();
  }

  /**
   * Fill in data to feed via SSE
   * @param event data to feed - in this case it is the generated private token {@link PrivateTokenGenerated}
   */
  void append(final Object event);

  /**
   * Tell event journal to {@link Sink} (write) events via SSE
   */
  void streamTo(final Sink sink, final Object referencing, final int start, final int limit);

  interface Sink {
    void consume(final List<Object> events, final int startId, final Object referencing);
  }

  class Provider {
    private EventJournal eventJournal;

    public EventJournal instance() {
      return eventJournal;
    }

    private void set(final EventJournal eventJournal) {
      this.eventJournal = eventJournal;
    }
  }
}
