package io.vlingo.cars.query;

import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.cars.query.view.CarView;
import io.vlingo.cars.query.view.CarsView;
import io.vlingo.symbio.store.state.StateStore;

public class CarQueriesActor extends StateStoreQueryActor implements CarQueries {
    public CarQueriesActor(StateStore stateStore) {
        super(stateStore);
    }

    @Override
    public Completes<CarsView> findAll() {
        return queryStateFor(CarsView.Id, CarsView.class);
    }

    @Override
    public Completes<CarView> findOne(String carId) {
        return queryStateFor(carId, CarView.class);
    }
}
