package io.examples.order.domain;

import io.examples.data.Identity;
import io.examples.order.domain.state.Disabled;
import io.examples.order.infra.journal.OrganizationInterest;
import io.vlingo.symbio.store.journal.Journal;
import io.vlingo.xoom.stepflow.State;
import io.vlingo.xoom.stepflow.StateTransition;
import io.vlingo.xoom.stepflow.StepFlow;

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

    public Organization define(StepFlow processor, Journal<String> journal) {
        OrganizationEvent definedEvent = new OrganizationEvent(status.name(), OrganizationStatus.ENABLED.name());
        return sendEvent(processor, definedEvent, stateTransition -> {
            // Then accept the state transition
            apply(stateTransition.getTo()).accept(stateTransition);
            journal.append("organization-" + getId(), 1, definedEvent, new OrganizationInterest(), this);
        });
    }

    public Organization enable(StepFlow processor, Journal<String> journal) {
        OrganizationEvent confirmEvent = new OrganizationEvent(status.name(), OrganizationStatus.ENABLED.name());
        return sendEvent(processor, confirmEvent, stateTransition -> {
            // Then accept the state transition
            apply(stateTransition.getTo()).accept(stateTransition);
            journal.append("organization-" + getId(), 1, confirmEvent, new OrganizationInterest(), this);

        });
    }

    public OrganizationEvent disable(StepFlow processor) {
        State targetState = new Disabled();
        OrganizationEvent event = new OrganizationEvent(status.name(), targetState.getName());
        sendEvent(processor, event, targetState);
        return event;
    }

    private Organization sendEvent(StepFlow processor, OrganizationEvent event, State targetState) {
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