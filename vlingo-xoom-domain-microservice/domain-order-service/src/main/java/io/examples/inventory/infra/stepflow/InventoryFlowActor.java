package io.examples.inventory.infra.stepflow;

import io.vlingo.common.Completes;
import io.vlingo.xoom.stepflow.FlowActor;
import io.vlingo.xoom.stepflow.State;

import java.util.List;

public class InventoryFlowActor extends FlowActor {

    public InventoryFlowActor(List<State> states) {
        super(states);
    }

    @Override
    public Completes<String> getName() {
        return completes().with("Inventory Flow");
    }
}
