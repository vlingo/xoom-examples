package io.vlingo.cars.model;

import io.vlingo.actors.ActorInstantiator;
import io.vlingo.common.Completes;
import io.vlingo.lattice.model.sourcing.EventSourced;

public class CarEntity extends EventSourced implements Car {
    private CarState state;

    static {
        registerConsumer(CarEntity.class, CarEvents.CarDefined.class, CarEntity::applyCarDefined);
        registerConsumer(CarEntity.class, CarEvents.CarRegistered.class, CarEntity::applyCarRegistered);
    }

    public CarEntity(String carId) {
        super(carId);
        this.state = CarState.from(carId);
    }

    @Override
    public Completes<CarState> defineWith(String type, String model, String registrationNumber) {
        return apply(CarEvents.CarDefined.with(state.carId, type, model, registrationNumber), () -> state);
    }

    @Override
    public Completes<CarState> registerWith(String registrationNumber) {
        return apply(CarEvents.CarRegistered.with(state.carId, registrationNumber), () -> state);
    }

    private void applyCarDefined(final CarEvents.CarDefined event) {
        this.state = state.defineWith(event.type, event.model, event.registrationNumber);
    }

    private void applyCarRegistered(final CarEvents.CarRegistered event) {
        this.state = state.registerWith(event.registrationNumber);
    }

    public static class CarEntityInstantiator implements ActorInstantiator<CarEntity> {
        private final String carId;

        public CarEntityInstantiator(String carId) {
            this.carId = carId;
        }

        @Override
        public CarEntity instantiate() {
            return new CarEntity(carId);
        }

        @Override
        public Class<CarEntity> type() {
            return CarEntity.class;
        }
    }
}
