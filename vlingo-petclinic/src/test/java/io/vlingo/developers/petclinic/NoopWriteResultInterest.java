package io.vlingo.developers.petclinic;

import io.vlingo.common.Outcome;
import io.vlingo.symbio.Source;
import io.vlingo.symbio.store.Result;
import io.vlingo.symbio.store.StorageException;
import io.vlingo.symbio.store.state.StateStore;

import java.util.List;

public class NoopWriteResultInterest implements StateStore.WriteResultInterest {
    @Override
    public <S, C> void writeResultedIn(Outcome<StorageException, Result> outcome, String id, S state, int stateVersion, List<Source<C>> sources, Object object) {
        // Noop
    }
}
