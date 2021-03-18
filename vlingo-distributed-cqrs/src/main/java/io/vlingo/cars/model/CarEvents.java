package io.vlingo.cars.model;

import io.vlingo.lattice.model.IdentifiedDomainEvent;

public class CarEvents {
    public static final class CarDefined extends IdentifiedDomainEvent {
        public final String carId;
        public final String type;
        public final String model;
        public final String registrationNumber;

        public static CarDefined with(String carId, String type, String model, String registrationNumber) {
            return new CarDefined(carId, type, model, registrationNumber);
        }

        private CarDefined(String carId, String type, String model, String registrationNumber) {
            this.carId = carId;
            this.type = type;
            this.model = model;
            this.registrationNumber = registrationNumber;
        }

        @Override
        public String identity() {
            return carId;
        }
    }
}
