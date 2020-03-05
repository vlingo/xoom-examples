package io.examples.order.domain.state;

import io.vlingo.xoom.stepflow.Transition;
import io.vlingo.xoom.stepflow.TransitionHandler;

import javax.inject.Singleton;

import static io.vlingo.xoom.stepflow.TransitionBuilder.from;
import static io.vlingo.xoom.stepflow.TransitionHandler.handle;

@Singleton
public class PaymentCreated extends OrderState<PaymentCreated> {

    private final PaymentPending paymentPending;

    public PaymentCreated(PaymentPending paymentPending) {
        this.paymentPending = paymentPending;
    }

    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[] {
                handle(from(this).to(paymentPending).then(Transition::logResult))
        };
    }

    @Override
    public String getName() {
        return OrderStatus.PAYMENT_CREATED.name();
    }
}
