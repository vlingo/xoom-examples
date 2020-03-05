package io.examples.order.domain;

import io.examples.data.Identity;
import io.examples.order.domain.state.Defined;
import io.examples.order.domain.state.Disabled;
import io.vlingo.xoom.stepflow.StepFlow;
import io.vlingo.xoom.stepflow.State;
import io.vlingo.xoom.stepflow.StateTransition;

import javax.persistence.Entity;
import java.util.Optional;
import java.util.function.Consumer;

@Entity
public class Organization extends Identity {

    private OrganizationStatus status = OrganizationStatus.DEFINED;

    public Organization() {
    }

    public OrganizationStatus getStatus() {
        return status;
    }

    public Organization define(StepFlow processor) {
        return sendEvent(processor, new Defined());
    }

    public Organization enable(StepFlow processor) {
        OrganizationEvent confirmEvent = new OrganizationEvent(status.name(), OrganizationStatus.ENABLED.name());
        return sendEvent(processor, confirmEvent, stateTransition -> {
            // TODO: Implement payment confirmation

            // Then accept the state transition
            apply(stateTransition.getTo()).accept(stateTransition);
        });
    }

    public Organization disable(StepFlow processor) {
        return sendEvent(processor, new Disabled());
    }

    private Organization sendEvent(StepFlow processor, State targetState) {
        OrganizationEvent event = new OrganizationEvent(status.name(), targetState.getName());
        return sendEvent(processor, event, apply(targetState));
    }

    private Organization sendEvent(StepFlow processor, OrganizationEvent event, Consumer<StateTransition> handler) {
        return Optional.ofNullable(processor.applyEvent(event)
                .andThenConsume(handler).otherwise(transition -> null).await())
                .map(transition -> this).orElseThrow(() -> new RuntimeException("The event with type ["
                        + event.getEventType() +
                        "] does not match a valid transition handler in the processor kernel."));
    }

    @SuppressWarnings("unchecked")
    private Consumer<StateTransition> apply(State newState) {
        return stateTransition -> {
            stateTransition.apply(this);
            this.status = OrganizationStatus.valueOf(stateTransition.getTargetName());
            this.version = newState.getVersion().toString();
        };
    }

    @Override
    public String toString() {
        return "Organization{" +
                "status=" + status +
                ", version='" + version + '\'' +
                "} " + super.toString();
    }


}