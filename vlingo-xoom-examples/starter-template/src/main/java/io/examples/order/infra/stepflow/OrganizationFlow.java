package io.examples.order.infra.stepflow;

import io.vlingo.xoom.stepflow.FlowActor;
import io.vlingo.xoom.stepflow.State;

import java.util.List;

public class OrganizationFlow extends FlowActor {

    public OrganizationFlow(List<State> states) {
        super(states);
    }
}
