package io.vlingo.xoom.examples.cars.query;

import io.vlingo.xoom.common.Completes;
import io.vlingo.xoom.lattice.query.StateStoreQueryActor;
import io.vlingo.xoom.examples.cars.query.view.CarView;
import io.vlingo.xoom.examples.cars.query.view.CarsView;
import io.vlingo.xoom.symbio.store.state.StateStore;

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
