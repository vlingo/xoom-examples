// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.eventjournal.counter.events;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapter;
import io.vlingo.symbio.Metadata;

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
}
