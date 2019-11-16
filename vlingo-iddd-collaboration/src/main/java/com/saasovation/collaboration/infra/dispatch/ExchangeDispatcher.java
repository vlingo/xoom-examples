package com.saasovation.collaboration.infra.dispatch;

import io.vlingo.common.serialization.JsonSerialization;
import io.vlingo.lattice.exchange.Exchange;
import io.vlingo.symbio.Entry;
import io.vlingo.symbio.State;
import io.vlingo.symbio.store.Result;
import io.vlingo.symbio.store.dispatch.ConfirmDispatchedResultInterest;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExchangeDispatcher implements Dispatcher<Dispatchable<Entry<String>, State<String>>>, ConfirmDispatchedResultInterest {
  private static final Logger logger = LoggerFactory.getLogger(ExchangeDispatcher.class);
  private final Exchange exchange;

  private DispatcherControl control;

  public ExchangeDispatcher(Exchange exchange) {
    this.exchange = exchange;
  }

  @Override
  public void controlWith(DispatcherControl control) {
    this.control = control;
  }

  @Override
  public void dispatch(Dispatchable<Entry<String>, State<String>> dispatchable) {
    logger.debug("Going to dispatch id {}", dispatchable.id());
    dispatchable.entries()
                .forEach(entry -> {
                  try {
                    this.exchange.send(JsonSerialization.deserialized(entry.entryData(), entry.typed()));
                  } catch (Exception e) {
                    logger.error("Entry {} of dispatch id {} will not be sent", dispatchable.id(), entry.id(), e);
                  }
                });
    this.control.confirmDispatched(dispatchable.id(), this);
  }

  @Override
  public void confirmDispatchedResultedIn(Result result, String dispatchId) {
    logger.debug("Dispatch id {} resulted in {}", dispatchId, result);
  }
}
