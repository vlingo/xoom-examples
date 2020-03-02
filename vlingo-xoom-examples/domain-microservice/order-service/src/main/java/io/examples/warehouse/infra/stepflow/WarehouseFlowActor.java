package io.examples.warehouse.infra.stepflow;

import io.vlingo.common.Completes;
import io.vlingo.xoom.stepflow.FlowActor;
import io.vlingo.xoom.stepflow.State;

import java.util.List;

public class WarehouseFlowActor extends FlowActor {

    public WarehouseFlowActor(List<State> states) {
        super(states);
    }

    @Override
    public Completes<String> getName() {
        return completes().with("Warehouse Flow");
    }
}
