// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.examples.eventjournal.counter.events;

import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.symbio.BaseEntry.TextEntry;
import io.vlingo.xoom.symbio.EntryAdapter;
import io.vlingo.xoom.symbio.Metadata;

public class CounterIncreasedAdapter implements EntryAdapter<CounterIncreased, TextEntry> {
    public CounterIncreasedAdapter() { }

    @Override
    public CounterIncreased fromEntry(TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData(), CounterIncreased.class);
    }
    
    @Override
    public TextEntry toEntry(final CounterIncreased source, final Metadata metadata) {
      return toEntry(source, source.uuid.toString());
    }
    
    @Override
    public TextEntry toEntry(final CounterIncreased source, final String id, final Metadata metadata) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(id, CounterIncreased.class, 1, serialization, metadata);
    }

    @Override
    public TextEntry toEntry(final CounterIncreased source, final int version, final String id, final Metadata metadata) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(id, CounterIncreased.class, 1, serialization, version, metadata);
    }
}
