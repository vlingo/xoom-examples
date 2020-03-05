package io.examples.order.domain.state;

import io.vlingo.xoom.stepflow.Transition;
import io.vlingo.xoom.stepflow.TransitionHandler;

import javax.inject.Singleton;

import static io.vlingo.xoom.stepflow.TransitionBuilder.from;
import static io.vlingo.xoom.stepflow.TransitionHandler.handle;

@Singleton
public class ReservationSucceeded extends OrderState<ReservationSucceeded> {

    private final PaymentCreated paymentCreated;

    public ReservationSucceeded(PaymentCreated paymentCreated) {
        this.paymentCreated = paymentCreated;
    }

    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[]{
                handle(from(this).to(paymentCreated).then(Transition::logResult))
        };
    }

    @Override
    public String getName() {
        return OrderStatus.RESERVATION_SUCCEEDED.name();
    }
}
