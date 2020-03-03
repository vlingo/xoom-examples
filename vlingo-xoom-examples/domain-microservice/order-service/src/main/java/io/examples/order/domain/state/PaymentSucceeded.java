package io.examples.order.domain.state;

import io.vlingo.xoom.stepflow.Transition;
import io.vlingo.xoom.stepflow.TransitionHandler;

import javax.inject.Singleton;

import static io.vlingo.xoom.stepflow.TransitionBuilder.from;
import static io.vlingo.xoom.stepflow.TransitionHandler.handle;

@Singleton
public class PaymentSucceeded extends OrderState<PaymentSucceeded> {

    private final OrderSucceeded orderSucceeded;

    public PaymentSucceeded(OrderSucceeded orderSucceeded) {
        this.orderSucceeded = orderSucceeded;
    }

    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[] {
            handle(from(this).to(orderSucceeded).then(Transition::logResult))
        };
    }

    @Override
    public String getName() {
        return OrderStatus.PAYMENT_SUCCEEDED.name();
    }
}
