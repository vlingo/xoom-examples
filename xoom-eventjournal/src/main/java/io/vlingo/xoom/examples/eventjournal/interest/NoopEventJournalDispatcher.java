package io.vlingo.xoom.examples.eventjournal.interest;

import io.vlingo.xoom.symbio.Entry;
import io.vlingo.xoom.symbio.State;
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;

public class NoopEventJournalDispatcher implements Dispatcher<Dispatchable<Entry<String>, State.TextState>> {

  @Override
  public void controlWith(final DispatcherControl control) {
    
  }

  @Override
  public void dispatch(final Dispatchable<Entry<String>, State.TextState> dispatchable) {
    
  }

}
