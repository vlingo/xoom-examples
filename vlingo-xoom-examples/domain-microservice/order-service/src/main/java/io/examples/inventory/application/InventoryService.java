package io.examples.inventory.application;

import io.examples.infra.StepFlowService;
import io.examples.inventory.domain.model.Inventory;
import io.examples.inventory.domain.model.InventoryStatus;
import io.examples.inventory.infra.repository.InventoryRepository;
import io.vlingo.common.Completes;
import io.vlingo.xoom.stepflow.StepFlow;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final StepFlowService stepFlowService;

    public InventoryService(InventoryRepository inventoryRepository, Provider<StepFlowService> processorService) {
        this.inventoryRepository = inventoryRepository;
        this.stepFlowService = processorService.get();
    }

    /**
     * Here we define an {@link Inventory} and begin the inventory workflow process by moving along the state of the
     * inventory from and to INVENTORY_CREATED.
     *
     * @param inventoryDefinition is the definition of the inventory that we will be creating
     * @return a completes publisher that will execute as the response is returned to the HTTP resource consumer
     */
    public Completes<Inventory> defineInventory(Inventory inventoryDefinition) {
        final StepFlow processor = stepFlowService.getInventoryContext().getProcessor();
        return Completes.withSuccess(inventoryDefinition)
                .andThen(inventory -> inventory.sendEvent(processor, InventoryStatus.INVENTORY_CREATED))
                .andThen(inventory -> inventory.sendEvent(processor, InventoryStatus.RESERVATION_PENDING))
                .andThenConsume(inventoryRepository::save)
                .andThenConsume(inventory -> queryInventory(inventory.getId()))
                .otherwise(inventory -> {
                    throw new RuntimeException("Could not define the inventory: " + inventory.toString());
                });
    }

    public Completes<Inventory> queryInventory(Long id) {
        return inventoryRepository.findById(id).map(Completes::withSuccess)
                .orElseThrow(() -> new RuntimeException("Could not find inventory"));
    }
}