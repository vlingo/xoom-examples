package io.vlingo.eventjournal.counter;

import com.google.gson.Gson;
import io.vlingo.actors.Actor;
import io.vlingo.common.Cancellable;
import io.vlingo.common.Completes;
import io.vlingo.common.Scheduled;
import io.vlingo.eventjournal.counter.events.CounterDecreased;
import io.vlingo.eventjournal.counter.events.CounterDecreasedAdapter;
import io.vlingo.eventjournal.counter.events.CounterIncreased;
import io.vlingo.eventjournal.counter.events.CounterIncreasedAdapter;
import io.vlingo.symbio.store.eventjournal.EventJournalReader;

import java.util.Optional;

public class CounterQueryActor extends Actor implements CounterQuery {
    private final EventJournalReader<String> streamReader;
    private final Gson gson;
    private final CounterIncreasedAdapter counterIncreasedAdapter;
    private final CounterDecreasedAdapter counterDecreasedAdapter;
    private final Cancellable cancellable;
    private Optional<Integer> currentCount;

    public CounterQueryActor(EventJournalReader<String> streamReader) {
        this.streamReader = streamReader;
        this.gson = new Gson();
        this.counterIncreasedAdapter = new CounterIncreasedAdapter();
        this.counterDecreasedAdapter = new CounterDecreasedAdapter();
        this.currentCount = Optional.empty();
        this.cancellable = scheduler().schedule(this::updateCounter, null, 0, 5);
        this.updateCounter(null, null);
    }

    @Override
    public Completes<Integer> counter() {
        return completes().with(currentCount.orElse(null));
    }

    private void updateCounter(Scheduled scheduled, Object data) {
        streamReader.readNext()
                .andThenInto(event -> Completes.withSuccess(event.asTextEvent()))
                .andThenConsume(event -> {
                    if (counterIncreasedAdapter.canDeserialize(event)) {
                        currentCount = Optional.of(counterIncreasedAdapter.deserialize(event).currentCounter);
                    } else if (counterDecreasedAdapter.canDeserialize(event)) {
                        currentCount = Optional.of(counterDecreasedAdapter.deserialize(event).currentCounter);
                    }
                });
    }

    @Override
    public void stop() {
        cancellable.cancel();
    }
}
