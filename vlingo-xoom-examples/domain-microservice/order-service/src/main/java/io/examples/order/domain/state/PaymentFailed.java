package io.examples.order.domain.state;

import io.vlingo.xoom.stepflow.Transition;
import io.vlingo.xoom.stepflow.TransitionHandler;

import javax.inject.Provider;
import javax.inject.Singleton;

import static io.vlingo.xoom.stepflow.TransitionBuilder.from;
import static io.vlingo.xoom.stepflow.TransitionHandler.handle;

@Singleton
public class PaymentFailed extends OrderState<PaymentFailed> {

    private final OrderFailed orderFailed;

    public PaymentFailed(Provider<OrderFailed> orderFailed) {
        this.orderFailed = orderFailed.get();
    }

    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[]{
                handle(from(this).to(orderFailed).then(Transition::logResult))
        };
    }

    @Override
    public String getName() {
        return OrderStatus.PAYMENT_FAILED.name();
    }
}
