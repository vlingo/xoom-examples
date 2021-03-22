package io.vlingo.developers.petclinic;

import io.vlingo.actors.testkit.AccessSafely;
import io.vlingo.common.Outcome;
import io.vlingo.symbio.Metadata;
import io.vlingo.symbio.store.Result;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.state.StateStore;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CountingReadResultInterest implements StateStore.ReadResultInterest {
    private AccessSafely access;
    private final Map<String, Object> items = new ConcurrentHashMap<>();

    @Override
    public <S> void readResultedIn(
            final Outcome<StorageException, Result> outcome,
            final String id,
            final S state,
            final int stateVersion,
            final Metadata metadata,
            final Object object) {
        access.writeUsing("item", id, state);
    }

    public AccessSafely afterCompleting(final int times) {
        access = AccessSafely.afterCompleting(times);
        access.writingWith("item", items::put);
        access.readingWith("item", items::get);
        return access;
    }
}
