package io.examples.infrastructure;

import io.examples.order.domain.OrderQueriesActor;
import io.vlingo.actors.Stage;
import io.vlingo.symbio.store.object.ObjectStore;

public class OrderQueryProvider {

    private final OrderQueries queries;
    private static OrderQueryProvider instance;

    public static OrderQueryProvider using(final Stage stage, final ObjectStore objectStore) {
        if(instance == null) {
            instance = new OrderQueryProvider(stage, objectStore);
        }
        return instance;
    }

    public static OrderQueryProvider instance() {
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    private OrderQueryProvider(final Stage stage, final ObjectStore objectStore) {
        this.queries = stage.actorFor(OrderQueries.class, OrderQueriesActor.class, objectStore);
    }

    public OrderQueries queries() {
        return queries;
    }

}
