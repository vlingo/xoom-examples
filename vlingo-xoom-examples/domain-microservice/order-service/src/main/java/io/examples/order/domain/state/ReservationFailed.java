package io.examples.order.domain.state;

import io.vlingo.xoom.stepflow.Transition;
import io.vlingo.xoom.stepflow.TransitionHandler;

import javax.inject.Provider;
import javax.inject.Singleton;

import static io.vlingo.xoom.stepflow.TransitionBuilder.from;
import static io.vlingo.xoom.stepflow.TransitionHandler.handle;

@Singleton
public class ReservationFailed extends OrderState<ReservationFailed> {

    private final OrderFailed orderFailed;

    public ReservationFailed(Provider<OrderFailed> orderFailed) {
        this.orderFailed = orderFailed.get();
    }

    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[] {
                handle(from(this).to(orderFailed).then(Transition::logResult))
        };
    }

    @Override
    public String getName() {
        return OrderStatus.RESERVATION_FAILED.name();
    }
}
