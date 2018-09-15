// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.backservice.infra.persistence;

import java.util.ArrayList;
import java.util.List;

import io.vlingo.actors.Actor;

public class EventJournalActor extends Actor implements EventJournal {
  private final List<Object> events;

  public EventJournalActor() {
    this.events = new ArrayList<>();
  }

  @Override
  public void append(final Object event) {
    System.out.println("APPENDED: " + event);
    events.add(event);
  }

  @Override
  public void streamTo(final Sink sink, final Object referencing, final int startId, final int limit) {
    final int lowestId = startId - 1;
    final int highestId = lowestId + limit - 1;
    final List<Object> substream = new ArrayList<>(limit);
    final int totalEvents = events.size();
    for (int index = lowestId; index <= highestId && index < totalEvents; ++index) {
      System.out.println("STREAMING: " + index);
      substream.add(events.get(index));
    }
    sink.consume(substream, startId, referencing);
  }
}
