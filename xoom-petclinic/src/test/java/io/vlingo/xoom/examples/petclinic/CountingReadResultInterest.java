package io.vlingo.xoom.examples.petclinic;

import io.vlingo.xoom.actors.testkit.AccessSafely;
import io.vlingo.xoom.common.Outcome;
import io.vlingo.xoom.symbio.Metadata;
import io.vlingo.xoom.symbio.store.Result;
import io.vlingo.xoom.symbio.store.StorageException;
import io.vlingo.xoom.symbio.store.state.StateStore;

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
