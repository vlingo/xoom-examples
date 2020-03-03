package io.examples.inventory.domain.model;

import io.vlingo.xoom.stepflow.Event;

public class InventoryEvent extends Event {

    public InventoryEvent(String source, String target) {
        super(source, target);
    }
}
