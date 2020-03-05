package io.examples.order.domain.state;

import io.vlingo.xoom.stepflow.TransitionHandler;

import javax.inject.Singleton;

@Singleton
public class OrderSucceeded extends OrderState<OrderSucceeded> {
    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[] {
        };
    }

    @Override
    public String getName() {
        return OrderStatus.ORDER_SUCCEEDED.name();
    }
}
