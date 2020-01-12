// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
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

public class CounterDecreasedAdapter implements EntryAdapter<CounterDecreased,TextEntry> {
    public CounterDecreasedAdapter() { }

    @Override
    public CounterDecreased fromEntry(final TextEntry entry) {
      return JsonSerialization.deserialized(entry.entryData(), CounterDecreased.class);
    }

    @Override
    public TextEntry toEntry(final CounterDecreased source, final Metadata metadata) {
      return toEntry(source, source.uuid.toString(), metadata);
    }

    @Override
    public TextEntry toEntry(final CounterDecreased source, final String id, final Metadata metadata) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(id, CounterIncreased.class, 1, serialization, metadata);
    }

    @Override
    public TextEntry toEntry(final CounterDecreased source, final int version, final String id, final Metadata metadata) {
      final String serialization = JsonSerialization.serialized(source);
      return new TextEntry(id, CounterIncreased.class, 1, serialization, version, metadata);
    }
}
