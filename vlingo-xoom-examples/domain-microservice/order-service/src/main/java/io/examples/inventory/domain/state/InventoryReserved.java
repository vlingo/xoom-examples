package io.examples.inventory.domain.state;

import io.examples.inventory.domain.model.InventoryStatus;
import io.vlingo.xoom.stepflow.Transition;
import io.vlingo.xoom.stepflow.TransitionHandler;

import javax.inject.Provider;
import javax.inject.Singleton;

import static io.vlingo.xoom.stepflow.TransitionBuilder.from;
import static io.vlingo.xoom.stepflow.TransitionHandler.handle;

@Singleton
public class InventoryReserved extends InventoryState<InventoryReserved> {

    private final InventoryReleased inventoryReleased;

    public InventoryReserved(Provider<InventoryReleased> inventoryReleased) {
        this.inventoryReleased = inventoryReleased.get();
    }

    @Override
    public TransitionHandler[] getTransitionHandlers() {
        return new TransitionHandler[]{
                handle(from(this).to(inventoryReleased).then(Transition::logResult))
        };
    }

    @Override
    public String getName() {
        return InventoryStatus.INVENTORY_RESERVED.name();
    }
}
