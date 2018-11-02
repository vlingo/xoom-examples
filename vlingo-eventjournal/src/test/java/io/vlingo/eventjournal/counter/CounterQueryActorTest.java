package io.vlingo.eventjournal.counter;

import io.vlingo.actors.Definition;
import io.vlingo.eventjournal.ActorTest;
import io.vlingo.eventjournal.counter.events.CounterDecreased;
import io.vlingo.eventjournal.counter.events.CounterDecreasedAdapter;
import io.vlingo.eventjournal.counter.events.CounterIncreased;
import io.vlingo.eventjournal.counter.events.CounterIncreasedAdapter;
import io.vlingo.symbio.Event;
import io.vlingo.symbio.store.eventjournal.EventJournalReader;
import org.junit.Before;
import org.junit.Test;

import static io.vlingo.common.Completes.withSuccess;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CounterQueryActorTest extends ActorTest {
    private static final int CURRENT_COUNTER = 5;
    private CounterQuery query;
    private EventJournalReader journalReader;
    private CounterIncreasedAdapter counterIncreasedAdapter;
    private CounterDecreasedAdapter counterDecreasedAdapter;

    @Before
    public void setUp() throws Exception {
        counterIncreasedAdapter = new CounterIncreasedAdapter();
        counterDecreasedAdapter = new CounterDecreasedAdapter();

        journalReader = mock(EventJournalReader.class);
    }

    @Test
    public void testThatUpdatesTheCounterFromIncreaseEventFromStream() {
        buildWithEvent(counterIncreasedAdapter.serialize(new CounterIncreased(CURRENT_COUNTER)));
        verify(journalReader, timeout(TIMEOUT).atLeastOnce()).readNext();

        int counter = query.counter().await();
        assertEquals(CURRENT_COUNTER, counter);
    }

    @Test
    public void testThatUpdatesTheCounterFromDecreaseEventFromStream() {
        buildWithEvent(counterDecreasedAdapter.serialize(new CounterDecreased(CURRENT_COUNTER)));
        verify(journalReader, timeout(TIMEOUT).atLeastOnce()).readNext();

        int counter = query.counter().await();
        assertEquals(CURRENT_COUNTER, counter);
    }

    private void buildWithEvent(Event<String> event) {
        when(journalReader.readNext()).thenReturn(withSuccess(event));

        query = world().actorFor(
                Definition.has(CounterQueryActor.class, Definition.parameters(journalReader)),
                CounterQuery.class
        );
    }
}
