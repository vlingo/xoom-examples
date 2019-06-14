// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.eventjournal.counter;

import static io.vlingo.common.Completes.withSuccess;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.eventjournal.ActorTest;
import io.vlingo.eventjournal.counter.events.CounterDecreased;
import io.vlingo.eventjournal.counter.events.CounterDecreasedAdapter;
import io.vlingo.eventjournal.counter.events.CounterIncreased;
import io.vlingo.eventjournal.counter.events.CounterIncreasedAdapter;
import io.vlingo.symbio.BaseEntry.TextEntry;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.store.journal.JournalReader;

public class CounterQueryActorTest extends ActorTest {
    private static final int CURRENT_COUNTER = 5;
    private CounterQuery query;
    private JournalReader<TextEntry> journalReader;
    private CounterIncreasedAdapter counterIncreasedAdapter;
    private CounterDecreasedAdapter counterDecreasedAdapter;
    private EntryAdapterProvider entryAdapterProvider;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        journalReader = mock(JournalReader.class);
        this.entryAdapterProvider = new EntryAdapterProvider();
        counterIncreasedAdapter = new CounterIncreasedAdapter();
        this.entryAdapterProvider.registerAdapter(CounterIncreased.class, counterIncreasedAdapter);
        counterDecreasedAdapter = new CounterDecreasedAdapter();
        this.entryAdapterProvider.registerAdapter(CounterDecreased.class, counterDecreasedAdapter);
    }

    @Test
    public void testThatUpdatesTheCounterFromIncreaseEventFromStream() {
        buildWithEvent(counterIncreasedAdapter.toEntry(new CounterIncreased(CURRENT_COUNTER)));
        verify(journalReader, timeout(TIMEOUT).atLeastOnce()).readNext();

        int counter = query.counter().await();
        assertEquals(CURRENT_COUNTER, counter);
    }

    @Test
    public void testThatUpdatesTheCounterFromDecreaseEventFromStream() {
        buildWithEvent(counterDecreasedAdapter.toEntry(new CounterDecreased(CURRENT_COUNTER)));
        verify(journalReader, timeout(TIMEOUT).atLeastOnce()).readNext();

        int result = query.counter().await();
        assertEquals(CURRENT_COUNTER, result);
    }

    private void buildWithEvent(TextEntry event) {
        when(journalReader.readNext()).thenReturn(withSuccess(event));

        query = world().actorFor(
                CounterQuery.class,
                Definition.has(CounterQueryActor.class, Definition.parameters(journalReader, entryAdapterProvider))
        );
    }
}
