package io.vlingo.xoom.examples.ecommerce.infra;

import io.vlingo.xoom.symbio.Entry;
import io.vlingo.xoom.symbio.State;
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;

import java.util.ArrayList;
import java.util.List;

public final class MockJournalDispatcher implements Dispatcher<Dispatchable<Entry<String>, State<?>>> {
    public List<Entry<String>> entries = new ArrayList<>();
    
    @Override
    public void controlWith(final DispatcherControl control) {
        
    }

    @Override
    public void dispatch(final Dispatchable<Entry<String>, State<?>> dispatchable) {
        this.entries.addAll(dispatchable.entries());
    }
}