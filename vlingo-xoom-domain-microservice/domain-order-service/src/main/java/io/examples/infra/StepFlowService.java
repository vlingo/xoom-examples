package io.examples.infra;

import io.examples.inventory.domain.state.InventoryState;
import io.examples.inventory.infra.stepflow.InventoryContext;
import io.examples.inventory.infra.stepflow.InventoryFlowActor;
import io.examples.order.domain.state.OrderState;
import io.examples.order.infra.stepflow.OrderContext;
import io.examples.order.infra.stepflow.OrderFlowActor;
import io.examples.warehouse.domain.state.WarehouseState;
import io.examples.warehouse.infra.stepflow.WarehouseContext;
import io.examples.warehouse.infra.stepflow.WarehouseFlowActor;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.event.ApplicationStartupEvent;
import io.vlingo.xoom.VlingoServer;
import io.vlingo.xoom.stepflow.StepFlow;
import io.vlingo.xoom.stepflow.FlowActor;
import io.vlingo.xoom.events.FlowCreatedEvent;
import io.vlingo.xoom.stepflow.State;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class StepFlowService implements ApplicationEventListener<ApplicationStartupEvent> {

    private final List<OrderState> orderStates;
    private final List<WarehouseState> warehouseStates;
    private final List<InventoryState> inventoryStates;
    private OrderContext orderProcessor;
    private WarehouseContext warehouseContext;
    private InventoryContext inventoryContext;

    public StepFlowService(List<OrderState> orderStates,
                           List<WarehouseState> warehouseStates,
                           List<InventoryState> inventoryStates) {
        this.orderStates = orderStates;
        this.warehouseStates = warehouseStates;
        this.inventoryStates = inventoryStates;
    }

    @Override
    public void onApplicationEvent(ApplicationStartupEvent event) {
        // Create a new order processor
        orderProcessor = new OrderContext(defineFlow(event,
                OrderFlowActor.class,
                orderStates.stream().map(state -> (State) state).collect(Collectors.toList()),
                "order"));

        // Create a new warehouse processor
        warehouseContext = new WarehouseContext(defineFlow(event,
                WarehouseFlowActor.class,
                warehouseStates.stream().map(state -> (State) state).collect(Collectors.toList()),
                "warehouse"));

        // Create a new inventory processor
        inventoryContext = new InventoryContext(defineFlow(event,
                InventoryFlowActor.class,
                inventoryStates.stream().map(state -> (State) state).collect(Collectors.toList()),
                "inventory"));
    }

    private <P extends StepFlow, A extends FlowActor> StepFlow defineFlow(ApplicationStartupEvent event,
                                                                          Class<A> actorClass,
                                                                          List<State> states,
                                                                          String flowName) {
        StepFlow flow = StepFlow.startWith(event.getSource()
                .getApplicationContext()
                .getBean(VlingoServer.class)
                .getVlingoScene()
                .getWorld()
                .stage(), actorClass, StepFlow.class, flowName, Collections.singletonList(states));

        event.getSource()
                .getApplicationContext()
                .publishEvent(new FlowCreatedEvent(flow, flowName));

        return flow;
    }

    public OrderContext getOrderContext() {
        return orderProcessor;
    }

    public WarehouseContext getWarehouseContext() {
        return warehouseContext;
    }

    public InventoryContext getInventoryContext() {
        return inventoryContext;
    }
}
