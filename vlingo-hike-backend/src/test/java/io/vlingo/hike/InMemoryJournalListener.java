package io.vlingo.hike;

import io.vlingo.actors.Actor;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.journal.JournalListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryJournalListener implements JournalListener<String> {
    public final List<Entry<String>> allEntries = new CopyOnWriteArrayList<>();
    public final List<State<String>> allSnapshots = new CopyOnWriteArrayList<>();
    public TestUntil until = TestUntil.happenings(1);

    @Override
    public void appended(final Entry<String> entry) {
        allEntries.add(entry);
        allSnapshots.add(null);
        until.happened();
    }

    @Override
    public void appendedWith(final Entry<String> entry, final State<String> snapshot) {
        allEntries.add(entry);
        allSnapshots.add(snapshot);
        until.happened();
    }

    @Override
    public void appendedAll(final List<Entry<String>> entries) {
        allEntries.addAll(entries);
        final List<State<String>> snapshots = new ArrayList<>(entries.size());
        for (int idx = 0; idx < entries.size(); ++idx) {
            snapshots.add(null);
        }
        allSnapshots.addAll(snapshots);
        until.happened();
    }

    @Override
    public void appendedAllWith(final List<Entry<String>> entries, final State<String> snapshot) {
        allEntries.addAll(entries);
        final List<State<String>> snapshots = new ArrayList<>(entries.size());
        for (int idx = 0; idx < (entries.size() - 1); ++idx) {
            snapshots.add(null);
        }
        snapshots.add(snapshot);
        allSnapshots.addAll(snapshots);
        until.happened();
    }

    public int confirmExpectedEntries(final int count, final int retries) {
        for (int idx = 0; idx < retries; ++idx) {
            if (allEntries.size() == count) {
                return count;
            }
            try { Thread.sleep(100); } catch (Exception e) { }
        }
        return allEntries.size();
    }
}