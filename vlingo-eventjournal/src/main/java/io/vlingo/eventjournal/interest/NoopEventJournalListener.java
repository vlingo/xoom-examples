package io.vlingo.eventjournal.interest;

import io.vlingo.symbio.Event;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.eventjournal.EventJournalListener;

import java.util.List;

public class NoopEventJournalListener implements EventJournalListener {
    @Override
    public void appended(Event event) {

    }

    @Override
    public void appendedWith(Event event, State snapshot) {

    }

    @Override
    public void appendedAllWith(List list, State snapshot) {

    }

    @Override
    public void appendedAll(List list) {

    }
}
