// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.backservice.infra.persistence;

import java.util.ArrayList;
import java.util.List;

import io.vlingo.xoom.actors.Actor;

public class EventJournalActor extends Actor implements EventJournal {
  private final List<Object> events;

  public EventJournalActor() {
    this.events = new ArrayList<>();
  }

  @Override
  public void append(final Object event) {
    logger().debug("APPENDED: " + event);
    events.add(event);
  }

  @Override
  public void streamTo(final Sink sink, final Object referencing, final int startId, final int limit) {
    final int lowestId = startId - 1;
    final int highestId = Math.max(1,lowestId + limit - 1); // We must be willing to send at least one
    final List<Object> substream = new ArrayList<>(limit);
    final int totalEvents = events.size();
    for (int index = lowestId; index <= highestId && (index < totalEvents); ++index) {
      logger().debug("STREAMING: " + index);
      substream.add(events.get(index));
    }
    sink.consume(substream, startId, referencing);
  }
}
