package io.vlingo.cars.query.view;

import java.util.Objects;

public class CarView {
    public final String carId;
    public final String type;
    public final String model;
    public final String registrationNumber;

    public static CarView with(String carId) {
        return new CarView(carId);
    }

    public static CarView with(String carId, String type, String model, String registrationNumber) {
        return new CarView(carId, type, model, registrationNumber);
    }

    public CarView mergeWith(String carId, String registrationNumber) {
        return this.carId.equals(carId)
                ? new CarView(this.carId, this.type, this.model, registrationNumber)
                : this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarView carView = (CarView) o;
        return carId.equals(carView.carId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(carId);
    }

    @Override
    public String toString() {
        return "CarView{" +
                "carId='" + carId + '\'' +
                ", type='" + type + '\'' +
                ", model='" + model + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }

    private CarView(String carId) {
        this(carId, "", "", "");
    }

    private CarView(String carId, String type, String model, String registrationNumber) {
        this.carId = carId;
        this.type = type;
        this.model = model;
        this.registrationNumber = registrationNumber;
    }
}
