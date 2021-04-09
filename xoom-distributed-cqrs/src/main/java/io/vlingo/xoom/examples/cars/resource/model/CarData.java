package io.vlingo.xoom.examples.cars.resource.model;

import io.vlingo.xoom.examples.cars.model.CarState;

public class CarData {
    public final String carId;
    public final String type;
    public final String model;
    public final String registrationNumber;

    public static CarData from(CarState state) {
        return new CarData(state.carId, state.type, state.model, state.registrationNumber);
    }

    private CarData(String carId, String type, String model, String registrationNumber) {
        this.carId = carId;
        this.type = type;
        this.model = model;
        this.registrationNumber = registrationNumber;
    }
}
