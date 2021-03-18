package io.vlingo.cars.model;

import java.util.Objects;

public class CarState {
    public final String carId;
    public final String type;
    public final String model;
    public final String registrationNumber;

    public static CarState from(String carId) {
        return new CarState(carId, null, null, null);
    }

    public static CarState from(String carId, String type, String model, String registrationNumber) {
        return new CarState(carId, type, model, registrationNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarState carState = (CarState) o;
        return carId.equals(carState.carId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carId);
    }

    private CarState(String carId, String type, String model, String registrationNumber) {
        this.carId = carId;
        this.type = type;
        this.model = model;
        this.registrationNumber = registrationNumber;
    }

    public CarState defineWith(String type, String model, String registrationNumber) {
        return new CarState(this.carId, type, model, registrationNumber);
    }

    @Override
    public String toString() {
        return "CarState{" +
                "carId='" + carId + '\'' +
                ", type='" + type + '\'' +
                ", model='" + model + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }
}
