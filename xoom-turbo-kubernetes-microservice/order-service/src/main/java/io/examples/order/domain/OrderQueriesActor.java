package io.examples.order.domain;

import io.examples.infrastructure.OrderQueries;
import io.vlingo.common.Completes;
import io.vlingo.lattice.query.StateObjectQueryActor;
import io.vlingo.symbio.store.MapQueryExpression;
import io.vlingo.symbio.store.QueryExpression;
import io.vlingo.symbio.store.object.ObjectStore;

import java.util.Set;

public class OrderQueriesActor extends StateObjectQueryActor implements OrderQueries {

    public OrderQueriesActor(final ObjectStore objectStore) {
        super(objectStore);
    }

    @Override
    public Completes<Set<OrderState>> allOrders() {
        final QueryExpression queryExpression =
                MapQueryExpression.using(OrderState.class, "findAll");

        return this.queryAll(Set.class, queryExpression, states -> (Set) states);
    }
}
