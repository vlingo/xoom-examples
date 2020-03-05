package io.examples.inventory.domain.state;

import io.examples.inventory.domain.model.InventoryStatus;
import io.vlingo.xoom.stepflow.Transition;
import io.vlingo.xoom.stepflow.TransitionHandler;

import javax.inject.Provider;
import javax.inject.Singleton;

import static io.vlingo.xoom.stepflow.TransitionBuilder.from;
import static io.vlingo.xoom.stepflow.TransitionHandler.handle;

@Singleton
public class ReservationPending extends InventoryState<ReservationPending> {

    private final ReservationConnected reservationConnected;

    public ReservationPending(Provider<ReservationConnected> reservationConnected) {
        this.reservationConnected = reservationConnected.get();
    }

    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[] {
                handle(from(this).to(reservationConnected).then(Transition::logResult))
        };
    }

    @Override
    public String getName() {
        return InventoryStatus.RESERVATION_PENDING.name();
    }
}
