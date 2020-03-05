package io.examples.order.domain;

import io.vlingo.xoom.stepflow.Event;
import io.vlingo.xoom.stepflow.State;

public class OrganizationEvent extends Event {

    private State source;
    private State target;
    private Organization aggregate;

    public OrganizationEvent(State source, State target, Organization aggregate) {
        this(source.getName(), target.getName());
        this.source = source;
        this.target = target;
        this.aggregate = aggregate;
    }

    public OrganizationEvent(String source, String target) {
        super(source, target);
    }

    public Organization getAggregate() {
        return aggregate;
    }

    public State getSource() {
        return source;
    }

    public State getTarget() {
        return target;
    }
}
