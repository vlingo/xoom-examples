package io.vlingo.eventjournal.query;

import com.google.gson.Gson;
import io.vlingo.actors.Actor;
import io.vlingo.common.Cancellable;
import io.vlingo.common.Completes;
import io.vlingo.common.Scheduled;
import io.vlingo.eventjournal.counter.events.CounterDecreasedEvent;
import io.vlingo.eventjournal.counter.events.CounterIncreasedEvent;
import io.vlingo.symbio.store.eventjournal.EventJournalReader;

import java.util.Optional;

public class CounterQueryActor extends Actor implements CounterQuery {
    private final EventJournalReader<String> streamReader;
    private final Gson gson;
    private Optional<Integer> currentCount;
    private Cancellable cancellable;

    public CounterQueryActor(EventJournalReader<String> streamReader) {
        this.streamReader = streamReader;
        this.gson = new Gson();
        this.currentCount = Optional.empty();
        this.cancellable = scheduler().schedule(this::updateCounter, null, 0, 25);
    }

    @Override
    public Completes<Integer> counter() {
        return completes().with(currentCount.orElse(null));
    }

    private void updateCounter(Scheduled scheduled, Object data) {
        streamReader.readNext()
                .andThenConsume(event -> {
                    if (event.type.equals(CounterIncreasedEvent.class.getCanonicalName())) {
                        currentCount = Optional.of(gson.fromJson(event.eventData, CounterIncreasedEvent.class).currentCounter);
                    } else if (event.type.equals(CounterDecreasedEvent.class.getCanonicalName())) {
                        currentCount = Optional.of(gson.fromJson(event.eventData, CounterDecreasedEvent.class).currentCounter);
                    }
                });
    }
}
