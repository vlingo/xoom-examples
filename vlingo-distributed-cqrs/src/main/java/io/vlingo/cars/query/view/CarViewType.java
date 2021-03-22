package io.vlingo.cars.query.view;

import io.vlingo.lattice.model.DomainEvent;

public enum CarViewType {
    CarDefined,

    Unmatched;

    public static CarViewType match(final DomainEvent event) {
        try {
            return CarViewType.valueOf(event.typeName());
        } catch (Exception e) {
            return Unmatched;
        }
    }
}
