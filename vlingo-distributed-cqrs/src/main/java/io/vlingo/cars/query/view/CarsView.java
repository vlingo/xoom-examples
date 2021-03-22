package io.vlingo.cars.query.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CarsView {
    public static final String Id = "root";

    private final List<CarItem> cars;

    public static CarsView empty() {
        return new CarsView();
    }

    private CarsView() {
        this.cars = new ArrayList<>();
    }

    private CarsView(List<CarItem> cars) {
        this.cars = cars;
    }

    public CarsView add(CarItem car) {
        if (cars.contains(car)) {
            return this;
        } else {
            CarsView result = new CarsView(new ArrayList<>(cars));
            result.cars.add(car);

            return result;
        }
    }

    public List<CarItem> all() {
        return Collections.unmodifiableList(cars);
    }

    @Override
    public String toString() {
        return "CarsView[" +
                "cars=" + cars +
                ']';
    }

    public static class CarItem {
        public final String carId;
        public final String type;
        public final String model;
        public final String registrationNumber;

        public static CarItem of(String carId, String type, String model, String registrationNumber) {
            return new CarItem(carId, type, model, registrationNumber);
        }

        private CarItem(String carId, String type, String model, String registrationNumber) {
            this.carId = carId;
            this.type = type;
            this.model = model;
            this.registrationNumber = registrationNumber;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CarItem carItem = (CarItem) o;
            return carId.equals(carItem.carId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(carId);
        }

        @Override
        public String toString() {
            return "CarItem{" +
                    "carId='" + carId + '\'' +
                    ", type='" + type + '\'' +
                    ", model='" + model + '\'' +
                    ", registrationNumber='" + registrationNumber + '\'' +
                    '}';
        }
    }
}
