package io.vlingo.eventjournal.counter;

import io.vlingo.actors.Definition;
import io.vlingo.eventjournal.ActorTest;
import io.vlingo.symbio.Event;
import io.vlingo.symbio.store.eventjournal.EventJournal;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class CounterActorTest extends ActorTest {
    private EventJournal journal;
    private String streamName;
    private Counter counter;

    @Before
    public void setUp() throws Exception {
        streamName = UUID.randomUUID().toString();
        journal = Mockito.mock(EventJournal.class);
        counter = world().actorFor(
                Definition.has(CounterActor.class, Definition.parameters(streamName, journal)),
                Counter.class
        );
    }

    @Test
    public void testThatOnIncreasePublishesAnEvent() {
        counter.increase();
        verify(journal, timeout(TIMEOUT)).append(eq(streamName), eq(1), any(Event.TextEvent.class), any(), counter);
    }

    @Test
    public void testThatOnDecreasePublishesAnEvent() {
        counter.increase();
        verify(journal, timeout(TIMEOUT)).append(eq(streamName), eq(1), any(Event.TextEvent.class), any(), counter);
    }
}
