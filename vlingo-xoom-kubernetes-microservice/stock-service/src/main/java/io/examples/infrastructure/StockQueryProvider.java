package io.examples.infrastructure;

import io.examples.stock.domain.StockQueriesActor;
import io.vlingo.actors.Stage;
import io.vlingo.symbio.store.object.ObjectStore;

public class StockQueryProvider {

    private final StockQueries queries;
    private static StockQueryProvider instance;

    public static StockQueryProvider using(final Stage stage, final ObjectStore objectStore) {
        if(instance == null) {
            instance = new StockQueryProvider(stage, objectStore);
        }
        return instance;
    }

    public static StockQueryProvider instance() {
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    private StockQueryProvider(final Stage stage, final ObjectStore objectStore) {
        this.queries = stage.actorFor(StockQueries.class, StockQueriesActor.class, objectStore);
    }

    public StockQueries queries() {
        return queries;
    }

}
