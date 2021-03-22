package io.vlingo.cars.model;

import io.vlingo.actors.Grid;
import io.vlingo.common.Completes;
import io.vlingo.common.identity.IdentityGeneratorType;

public interface Car {

    static Completes<CarState> with(Grid grid, String type, String model, String registrationNumber) {
        final String carId = IdentityGeneratorType.Random.generate().toString();
        final Car car = grid.actorFor(Car.class, CarEntity.class, carId);

        return car.defineWith(type, model, registrationNumber);
    }

    Completes<CarState> defineWith(String type, String model, String registrationNumber);
}
