package io.examples.inventory.domain.state;

import io.examples.inventory.domain.model.InventoryStatus;
import io.vlingo.xoom.stepflow.Transition;
import io.vlingo.xoom.stepflow.TransitionHandler;

import javax.inject.Provider;
import javax.inject.Singleton;

import static io.vlingo.xoom.stepflow.TransitionBuilder.from;
import static io.vlingo.xoom.stepflow.TransitionHandler.handle;

@Singleton
public class InventoryCreated extends InventoryState<InventoryCreated> {

    private final ReservationPending reservationPending;

    public InventoryCreated(Provider<ReservationPending> reservationPending) {
        this.reservationPending = reservationPending.get();
    }

    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[]{
                handle(from(this).to(reservationPending).then(Transition::logResult))
        };
    }

    @Override
    public String getName() {
        return InventoryStatus.INVENTORY_CREATED.name();
    }
}
