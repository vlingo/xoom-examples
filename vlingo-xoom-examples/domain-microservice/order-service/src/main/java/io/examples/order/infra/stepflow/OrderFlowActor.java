package io.examples.order.infra.stepflow;

import io.vlingo.common.Completes;
import io.vlingo.xoom.stepflow.FlowActor;
import io.vlingo.xoom.stepflow.State;

import java.util.List;

public class OrderFlowActor extends FlowActor {

    public OrderFlowActor(List<State> states) {
        super(states);
    }

    @Override
    public Completes<String> getName() {
        return completes().with("Order Flow");
    }
}
