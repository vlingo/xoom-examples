// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.backservice.infra.persistence;

import java.util.List;

import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;

public interface EventJournal {
  public static Provider provider = new Provider();
  public static Provider provider() { return provider; }

  public static EventJournal startWith(final Stage stage) {
    provider.set(stage.actorFor(Definition.has(EventJournalActor.class, Definition.NoParameters), EventJournal.class));
    return provider.instance();
  }

  void append(final Object event);
  void streamTo(final Sink sink, final Object referencing, final int start, final int limit);

  public interface Sink {
    void consume(final List<Object> events, final int startId, final Object referencing);
  }

  public class Provider {
    private EventJournal eventJournal;

    public EventJournal instance() {
      return eventJournal;
    }

    private void set(final EventJournal eventJournal) {
      this.eventJournal = eventJournal;
    }
  }
}
