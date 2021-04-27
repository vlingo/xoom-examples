package io.vlingo.xoom.examples.cars.query;

import io.vlingo.xoom.lattice.model.DomainEvent;
import io.vlingo.xoom.lattice.model.IdentifiedDomainEvent;
import io.vlingo.xoom.lattice.model.projection.Projectable;
import io.vlingo.xoom.lattice.model.projection.StateStoreProjectionActor;
import io.vlingo.xoom.examples.cars.model.CarEvents;
import io.vlingo.xoom.examples.cars.query.view.CarView;
import io.vlingo.xoom.examples.cars.query.view.CarViewType;
import io.vlingo.xoom.symbio.Entry;
import io.vlingo.xoom.symbio.store.state.StateStore;

import java.util.ArrayList;
import java.util.List;

public class CarProjection extends StateStoreProjectionActor<CarView> {
    private String dataId;

    /**
     * Keep state between prepareForMergeWith(...) and merge(...) methods.
     * This list holds the latest events occurred which are not merged yet.
     */
    private final List<IdentifiedDomainEvent> events;

    public CarProjection(StateStore stateStore) {
        super(stateStore);

        this.events = new ArrayList<>(2);
    }

    @Override
    protected CarView currentDataFor(Projectable projectable) {
        return CarView.with(projectable.dataId());
    }

    @Override
    protected String dataIdFor(Projectable projectable) {
        dataId = events.get(0).identity();

        return dataId;
    }

    @Override
    protected CarView merge(CarView previousData, int previousVersion, CarView currentData, int currentVersion) {
        return previousData == null
                ? mergeEventsInto(currentData)
                : mergeEventsInto(previousData);
    }

    @Override
    protected void prepareForMergeWith(Projectable projectable) {
        events.clear();

        for (final Entry<?> entry : projectable.entries()) {
            events.add(entryAdapter().anyTypeFromEntry(entry));
        }
    }

    private CarView mergeEventsInto(final CarView initialData) {
        CarView mergedData = initialData;

        for (DomainEvent event : events) {
            switch (CarViewType.match(event)) {
                case CarDefined:
                    CarEvents.CarDefined defined = typed(event);
                    mergedData = CarView.with(defined.carId, defined.type, defined.model, defined.registrationNumber);
                    break;
                case CarRegistered:
                    CarEvents.CarRegistered registered = typed(event);
                    mergedData = mergedData.mergeWith(registered.carId, registered.registrationNumber);
                    break;
                case Unmatched:
                    logger().warn("Event of type " + event.typeName() + " was not matched.");
                    break;
            }
        }

        logger().debug("PROJECTED: " + mergedData);

        return  mergedData;
    }
}
