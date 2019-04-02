package io.vlingo.examples.ecommerce.infra;

import java.util.ArrayList;
import java.util.List;

import io.vlingo.symbio.Entry;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.journal.JournalListener;

public final class MockJournalListener implements JournalListener<String> {
    public List<Entry<String>> entries = new ArrayList<>();

    @Override
    public void appended(Entry<String> entry) {
        this.entries.add(entry);
    }

    @Override
    public void appendedWith(Entry<String> entry, State<String> snapshot) {
        this.entries.add(entry);
    }

    @Override
    public void appendedAll(List<Entry<String>> entries) {
        this.entries.addAll(entries);
    }

    @Override
    public void appendedAllWith(List<Entry<String>> entries, State<String> snapshot) {
        this.entries.addAll(entries);
    }
}