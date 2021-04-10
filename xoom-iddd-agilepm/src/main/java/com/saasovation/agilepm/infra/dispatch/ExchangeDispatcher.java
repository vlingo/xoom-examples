package com.saasovation.agilepm.infra.dispatch;

import io.vlingo.xoom.common.serialization.JsonSerialization;
import io.vlingo.xoom.lattice.exchange.Exchange;
import io.vlingo.xoom.symbio.Entry;
import io.vlingo.xoom.symbio.State;
import io.vlingo.xoom.symbio.store.Result;
import io.vlingo.xoom.symbio.store.dispatch.ConfirmDispatchedResultInterest;
import io.vlingo.xoom.symbio.store.dispatch.Dispatchable;
import io.vlingo.xoom.symbio.store.dispatch.Dispatcher;
import io.vlingo.xoom.symbio.store.dispatch.DispatcherControl;
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

        for (Entry<String> entry : dispatchable.entries()) {
            try {
                this.exchange.send(JsonSerialization.deserialized(entry.entryData(), entry.typed()));
            } catch (Exception e) {
                logger.error("Entry {} of dispatch id {} will not be sent", dispatchable.id(), entry.id(), e);
            }
        }
        this.control.confirmDispatched(dispatchable.id(), this);
    }

    @Override
    public void confirmDispatchedResultedIn(Result result, String dispatchId) {
        logger.debug("Dispatch id {} resulted in {}", dispatchId, result);
    }
}
