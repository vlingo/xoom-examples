package io.vlingo.cars.query;

import io.vlingo.common.Completes;
import io.vlingo.cars.query.view.CarView;
import io.vlingo.cars.query.view.CarsView;

public interface CarQueries {
    Completes<CarsView> findAll();
    Completes<CarView> findOne(String carId);
}
