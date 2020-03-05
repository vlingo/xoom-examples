package io.examples.order.infra.stepflow;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.event.ApplicationStartupEvent;
import io.vlingo.xoom.VlingoServer;
import io.vlingo.xoom.stepflow.StepFlow;
import io.vlingo.xoom.events.FlowCreatedEvent;
import io.vlingo.xoom.stepflow.State;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class StepFlowService implements ApplicationEventListener<ApplicationStartupEvent> {

    private final State[] states;
    private StepFlow processor;

    public StepFlowService(State[] states) {
        this.states = states;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onApplicationEvent(ApplicationStartupEvent event) {
        processor = StepFlow.startWith(event.getSource()
                        .getApplicationContext()
                        .getBean(VlingoServer.class)
                        .getVlingoScene().getWorld().stage(), OrganizationFlow.class,
                "OrganizationFlow", Stream.of(Arrays.asList(states)).collect(Collectors.toList()));

        event.getSource().getApplicationContext()
                .publishEvent(new FlowCreatedEvent(processor, "organization"));
    }

    public StepFlow getFlow() {
        return processor;
    }
}
