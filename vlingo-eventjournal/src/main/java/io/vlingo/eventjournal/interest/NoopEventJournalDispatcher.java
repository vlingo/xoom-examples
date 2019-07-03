package io.vlingo.eventjournal.interest;

import io.vlingo.symbio.Entry;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;

public class NoopEventJournalDispatcher implements Dispatcher<Dispatchable<Entry<String>, State.TextState>> {

  @Override
  public void controlWith(final DispatcherControl control) {
    
  }

  @Override
  public void dispatch(final Dispatchable<Entry<String>, State.TextState> dispatchable) {
    
  }

}
