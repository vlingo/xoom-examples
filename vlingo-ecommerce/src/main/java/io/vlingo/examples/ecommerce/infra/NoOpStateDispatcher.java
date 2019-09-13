package io.vlingo.examples.ecommerce.infra;

import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;

public class NoOpStateDispatcher implements Dispatcher {

    @Override
    public void controlWith(DispatcherControl dispatcherControl) {

    }

    @Override
    public void dispatch(Dispatchable dispatchable) {

    }
}
