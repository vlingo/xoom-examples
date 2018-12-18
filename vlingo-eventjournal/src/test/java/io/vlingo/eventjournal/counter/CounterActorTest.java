package io.vlingo.eventjournal.counter;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.vlingo.actors.Definition;
import io.vlingo.eventjournal.ActorTest;
import io.vlingo.symbio.Source;
import io.vlingo.symbio.store.journal.Journal;

public class CounterActorTest extends ActorTest {
    private Journal<String> journal;
    private String streamName;
    private Counter counter;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        streamName = UUID.randomUUID().toString();
        journal = Mockito.mock(Journal.class);
        counter = world().actorFor(
                Definition.has(CounterActor.class, Definition.parameters(streamName, journal)),
                Counter.class
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testThatOnIncreasePublishesAnEvent() {
        counter.increase();
        verify(journal, timeout(TIMEOUT)).append(eq(streamName), eq(1), any(Source.class), any(), any());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testThatOnDecreasePublishesAnEvent() {
        counter.increase();
        verify(journal, timeout(TIMEOUT)).append(eq(streamName), eq(1), any(Source.class), any(), any());
    }
}
