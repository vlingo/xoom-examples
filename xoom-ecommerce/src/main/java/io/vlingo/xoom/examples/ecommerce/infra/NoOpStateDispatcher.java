package io.vlingo.xoom.examples.ecommerce.infra;

import io.vlingo.xoom.symbio.store.dispatch.Dispatchable;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;

@SuppressWarnings("rawtypes")
public class NoOpStateDispatcher implements Dispatcher {

    @Override
    public void controlWith(DispatcherControl dispatcherControl) {

    }

    @Override
    public void dispatch(Dispatchable dispatchable) {

    }
}
