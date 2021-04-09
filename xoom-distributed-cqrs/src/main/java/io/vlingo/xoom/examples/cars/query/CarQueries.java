package io.vlingo.xoom.examples.cars.query;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.examples.cars.query.view.CarView;
import io.vlingo.xoom.examples.cars.query.view.CarsView;

public interface CarQueries {
    Completes<CarsView> findAll();
    Completes<CarView> findOne(String carId);
}
