package io.examples.order.infra.stepflow;

import io.vlingo.xoom.stepflow.StepFlow;

public class OrderContext {
    private final StepFlow flow;

    public OrderContext(StepFlow flow) {
        this.flow = flow;
    }

    public StepFlow getFlow() {
        return flow;
    }
}
