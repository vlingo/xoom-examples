package io.examples.warehouse.application;

import io.examples.infra.StepFlowService;
import io.examples.warehouse.domain.model.Warehouse;
import io.examples.warehouse.domain.model.WarehouseStatus;
import io.examples.warehouse.infra.WarehouseRepository;
import io.vlingo.common.Completes;
import io.vlingo.xoom.stepflow.StepFlow;

import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final StepFlowService stepFlowService;

    public WarehouseService(WarehouseRepository warehouseRepository, Provider<StepFlowService> processorService) {
        this.warehouseRepository = warehouseRepository;
        this.stepFlowService = processorService.get();
    }

    /**
     * Here we define an {@link Warehouse} and begin the warehouse workflow process by moving along the state of the
     * warehouse from and to WAREHOUSE_CREATED.
     *
     * @param warehouseDefinition is the definition of the warehouse that we will be creating
     * @return a completes publisher that will execute as the response is returned to the HTTP resource consumer
     */
    public Completes<Warehouse> defineWarehouse(Warehouse warehouseDefinition) {
        final StepFlow processor = stepFlowService.getWarehouseContext().getProcessor();
        return Completes.withSuccess(warehouseDefinition)
                .andThen(warehouse -> warehouse.sendEvent(processor, WarehouseStatus.WAREHOUSE_CREATED))
                .andThenConsume(warehouseRepository::save)
                .andThenConsume(warehouse -> queryWarehouse(warehouse.getId()))
                .otherwise(warehouse -> {
                    throw new RuntimeException("Could not define the warehouse: " + warehouse.toString());
                });
    }

    public Completes<Warehouse> queryWarehouse(Long id) {
        return warehouseRepository.findById(id).map(Completes::withSuccess)
                .orElseThrow(() -> new RuntimeException("Could not find warehouse"));
    }
}
