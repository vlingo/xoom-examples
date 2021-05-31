package io.vlingo.xoom.examples.petclinic;

import io.vlingo.xoom.common.Outcome;
import io.vlingo.xoom.symbio.Source;
import io.vlingo.xoom.symbio.store.Result;
import io.vlingo.xoom.symbio.store.StorageException;
import io.vlingo.xoom.symbio.store.state.StateStore;

import java.util.List;

public class NoopWriteResultInterest implements StateStore.WriteResultInterest {
    @Override
    public <S, C> void writeResultedIn(Outcome<StorageException, Result> outcome, String id, S state, int stateVersion, List<Source<C>> sources, Object object) {
        // Noop
    }
}
