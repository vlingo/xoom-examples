package io.examples.order.domain;

import io.vlingo.xoom.stepflow.Event;

public class OrganizationEvent extends Event {

    public OrganizationEvent(String source, String target) {
        super(source, target);
    }
}
