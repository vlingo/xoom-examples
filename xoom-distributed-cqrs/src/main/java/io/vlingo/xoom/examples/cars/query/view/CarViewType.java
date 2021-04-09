package io.vlingo.xoom.examples.cars.query.view;

import io.vlingo.xoom.lattice.model.DomainEvent;

public enum CarViewType {
    CarDefined,
    CarRegistered,

    Unmatched;

    public static CarViewType match(final DomainEvent event) {
        try {
            return CarViewType.valueOf(event.typeName());
        } catch (Exception e) {
            return Unmatched;
        }
    }
}
