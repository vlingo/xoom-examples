package io.vlingo.hike.infrastructure;

import io.vlingo.hike.domain.route.RouteActor;
import io.vlingo.hike.domain.route.events.WalkedThrough;
import io.vlingo.hike.infrastructure.route.WalkedThroughAdapter;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry;
import io.vlingo.lattice.model.sourcing.SourcedTypeRegistry.Info;
import io.vlingo.symbio.store.journal.Journal;

public class SourcedRegistration {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> void registerAllWith(final SourcedTypeRegistry registry, final Journal<T> journal) {
        registry.register(new Info(journal, RouteActor.class, RouteActor.class.getSimpleName()));

        registry.info(RouteActor.class)
                .register(WalkedThrough.class, WalkedThroughAdapter.instance(), journal::registerAdapter);
    }
}
