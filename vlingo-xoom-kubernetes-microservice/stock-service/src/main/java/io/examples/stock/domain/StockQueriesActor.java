package io.examples.stock.domain;

import io.examples.infrastructure.StockQueries;
import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateObjectQueryActor;
import io.vlingo.symbio.store.MapQueryExpression;
import io.vlingo.symbio.store.QueryExpression;
import io.vlingo.symbio.store.object.ObjectStore;

import java.util.Set;
import java.util.function.Function;

public class StockQueriesActor extends StateObjectQueryActor implements StockQueries {

    public StockQueriesActor(final ObjectStore objectStore) {
        super(objectStore);
    }

    @Override
    public Completes<StockState> queryByLocation(final Location location) {
        final QueryExpression queryExpression =
                MapQueryExpression.using(StockState.class, "findAll");

        final Function<Set<StockState>, StockState> filter = states ->
                states.stream().filter(state -> state.locatedIn(location)).findFirst().get();

        return this.queryAll(Set.class, queryExpression, filter);
    }

}
