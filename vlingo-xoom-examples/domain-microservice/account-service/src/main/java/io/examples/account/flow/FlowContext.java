package io.examples.account.flow;

import io.micronaut.context.event.ApplicationEventListener;
import io.vlingo.xoom.stepflow.StepFlow;
import io.vlingo.xoom.events.FlowCreatedEvent;
import io.vlingo.xoom.events.SceneStartedEvent;
import io.vlingo.xoom.stepflow.State;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Collections;

@Singleton
public class FlowContext implements ApplicationEventListener<SceneStartedEvent> {

    private final State[] states;
    private StepFlow processor;

    public FlowContext(State[] states) {
        this.states = states;
    }

    @Override
    public void onApplicationEvent(SceneStartedEvent event) {
        processor = StepFlow.startWith(event.getSource().getWorld().stage(),
                AccountFlow.class, "AccountProcessor",
                Collections.singletonList(Arrays.asList(states)));

        event.getSource()
                .getApplicationContext()
                .publishEvent(new FlowCreatedEvent(processor, "account"));
    }

    public StepFlow getProcessor() {
        return processor;
    }
}
