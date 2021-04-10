package com.saasovation.collaboration.infra.exchange;

import io.vlingo.xoom.lattice.exchange.ExchangeReceiver;

public class NoOpReceiver<L> implements ExchangeReceiver<L> {
    @Override
    public void receive(L message) {
        //do nothing
    }
}
