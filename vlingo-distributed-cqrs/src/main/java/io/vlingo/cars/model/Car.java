package io.vlingo.cars.model;

import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.common.identity.IdentityGeneratorType;

public interface Car {

    static Completes<CarState> with(Stage stage, String type, String model, String registrationNumber) {
        final String carId = IdentityGeneratorType.Random.generate().toString();
        final String actorName = "C:" + carId;

        // final Address address = stage.addressFactory().from(carId, actorName);
        // final Definition definition = Definition.has(CarEntity.class, new CarEntity.CarEntityInstantiator(carId), actorName);
        // final Car car = stage.actorFor(Car.class, definition, address);

        final Car car = stage.actorFor(Car.class, CarEntity.class, carId);

        return car.defineWith(type, model, registrationNumber);
    }

    Completes<CarState> defineWith(String type, String model, String registrationNumber);
}
